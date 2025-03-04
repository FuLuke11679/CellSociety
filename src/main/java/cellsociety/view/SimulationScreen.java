package cellsociety.view;

import cellsociety.model.grid.CellShapeFactory;
import cellsociety.model.grid.EdgeFactory;
import cellsociety.model.grid.Grid;
import cellsociety.model.grid.NeighborhoodFactory;
import cellsociety.model.ruleset.Ruleset;
import cellsociety.model.ruleset.RulesetFactory;
import cellsociety.parser.XMLParser;
import cellsociety.view.shapes.ShapeFactory;
import java.io.File;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SimulationScreen {
  private SimulationController myController;
  private XMLParser myParser;
  private GridView myGridView;
  private Grid myGrid;
  private Scene simScene;
  private int width = 800;
  private int height = 800;
  private static double SECOND_DELAY = 0.8;

  public SimulationScreen(File file, SimulationController controller){
    myController = controller;
    parseFile(file);

  }

  private void parseFile(File file){
    ResourceBundle simInfo = ResourceBundle.getBundle("SimInfo", myController.getLocale());
    try {
      if (file == null || file.length() == 0) {
        throw new IllegalArgumentException(simInfo.getString("invalid_file"));
      }

      myParser = new XMLParser(file);
      Ruleset ruleset = getRuleset();
      myGrid = ruleset.createGrid(myParser.getRows(), myParser.getColumns(), myParser.getInitialStates());
      myGrid.setEdgeHandler(EdgeFactory.createEdgeHandler(myParser.getEdgeType()));
      myGrid.setNeighborhoodStrategy(NeighborhoodFactory.createNeighborhoodStrategy(myParser.getNeighborhoodType()));
      myGrid.setCellShape(CellShapeFactory.createCellShape(myParser.getCellShape()));
      myGridView = new GridView(
          myParser.getRows(),
          myParser.getColumns(),
          myParser.getSimType(),
          myParser.getTitle(),
          myParser.getAuthor(),
          myParser.getDescription(),
          myGrid,
          myController.getScheme(),
          myController.getLocale());

      BorderPane layout = initializeLayout(simInfo);
      layout.getStyleClass().add("layout");
      simScene = new Scene(layout, width, height);
      myController.setStage(simScene);
      //myController.setStage(new Scene(layout, width, height));
    } catch (IllegalArgumentException e) {
      myController.showMessage(simInfo.getString("invalid_config") + e.getMessage());
    } catch (Exception e) {
      myController.showMessage(simInfo.getString("load_error") + e.getMessage());
    }
  }

  private Ruleset getRuleset() {
    return RulesetFactory.createRuleset(myParser.getSimType(), myParser.getSimVarsMap());
  }

  /**
   * Initializes simulation layout after splash screen
   * @param simInfo: resource bundle containing hardcoded simulation text
   * @return : Organized BorderPane holding nodes for simulation
   */
  private BorderPane initializeLayout(ResourceBundle simInfo) {
    BorderPane layout = new BorderPane();
    HBox centerWrapper = new HBox(myGridView.getScene().getRoot());
    centerWrapper.setAlignment(Pos.CENTER);
    //centerWrapper.setId("grid");
    centerWrapper.getStyleClass().add("grid");
    layout.setCenter(centerWrapper);

    Button startButton = new Button(simInfo.getString("start"));
    startButton.getStyleClass().add("start-button");
    Button pauseButton = new Button(simInfo.getString("pause"));
    pauseButton.getStyleClass().add("pause-button");
    Button saveButton = new Button(simInfo.getString("save"));
    saveButton.getStyleClass().add("save-button");
    Button resetButton = new Button(simInfo.getString("reset"));
    resetButton.getStyleClass().add("reset-button");
    Button loadButton = new Button(simInfo.getString("load_file"));
    loadButton.getStyleClass().add("load-button");

    startButton.setOnAction(e -> myController.startSimulation());
    pauseButton.setOnAction(e -> {
      if (myController.getSimLoop() != null) {
        if (myController.getSimLoop().getStatus() == Animation.Status.RUNNING) {
          myController.getSimLoop().pause();  // Pause without destroying it

        }
        else if (myController.getSimLoop().getStatus() == Animation.Status.PAUSED) {  //REMOVE?
          myController.getSimLoop().play();  // Resume from where it left off
        }
      }
    });

    saveButton.setOnAction(e -> myController.saveSimulation(simInfo));
    resetButton.setOnAction(e -> myController.resetSimulation());
    loadButton.setOnAction(e -> {
      File newFile = myController.getFileChooser().showOpenDialog(myController.getStage());
      if (newFile != null) {
        myController.loadSimulation(newFile);
      }
    });

    Slider speedSlider = new Slider(0.1, 2.0, SECOND_DELAY);
    speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
      SECOND_DELAY = 2.1 - newVal.doubleValue();  // Update speed delay

      if (myController.getSimLoop() != null) {
        myController.getSimLoop().setRate(1.0 / SECOND_DELAY);  // Adjust the playback speed
      }
    });
    speedSlider.getStyleClass().add("speed-slider");

    HBox controls = new HBox(10, startButton, pauseButton, saveButton, resetButton, loadButton, new Label("Speed:"), speedSlider);
    controls.setId("simControls");
    layout.setBottom(controls);

    VBox settingsPanel = new VBox(15);
    settingsPanel.setStyle("-fx-padding: 10;");

    Label settingsTitle = new Label("Grid Settings");

// Edge Dropdown
    ComboBox<String> edgeDropdown = new ComboBox<>();
    edgeDropdown.getItems().addAll("Toroidal", "Mirror", "Infinite");
    edgeDropdown.setValue(myParser.getEdgeType());
    edgeDropdown.setOnAction(e -> {
      myGrid.setEdgeHandler(EdgeFactory.createEdgeHandler(edgeDropdown.getValue()));
    });

// Neighborhood Dropdown
    ComboBox<String> neighborhoodDropdown = new ComboBox<>();
    neighborhoodDropdown.getItems().addAll("VonNeumann", "ExtendedMoore");
    neighborhoodDropdown.setValue(myParser.getNeighborhoodType());
    neighborhoodDropdown.setOnAction(e -> {
      myGrid.setNeighborhoodStrategy(NeighborhoodFactory.createNeighborhoodStrategy(neighborhoodDropdown.getValue()));
    });

// Shape Dropdown (Dynamic from Factory)
    ComboBox<String> shapeDropdown = new ComboBox<>();
    shapeDropdown.getItems().addAll(ShapeFactory.getAvailableShapes()); // Dynamically fetch shapes
    shapeDropdown.setValue(myParser.getCellShape());
    shapeDropdown.setOnAction(e -> {
      String selectedShape = shapeDropdown.getValue();

      if (selectedShape == null || selectedShape.trim().isEmpty()) {
        System.err.println("Error: Selected shape is null or empty.");
        return;
      }

      String fullyQualifiedShape = ShapeFactory.getFullyQualifiedName(selectedShape);

      myGrid.setCellShape(CellShapeFactory.createCellShape(fullyQualifiedShape));
      myGridView.redrawGrid(myParser.getRows(), myParser.getColumns(), fullyQualifiedShape);

      System.out.println("Redrawing grid with shape: " + fullyQualifiedShape);
    });
    settingsPanel.getChildren().addAll(
        settingsTitle,
        new Label("Edge Type:"), edgeDropdown,
        new Label("Neighborhood Type:"), neighborhoodDropdown,
        new Label("Cell Shape:"), shapeDropdown
    );

    layout.setRight(settingsPanel); // Place settings on the right side
    return layout;
  }

  public void update(){
    myGrid.update();
    myGridView.update();
  }

  public XMLParser getMyParser() {
    return myParser;
  }

  public Scene getSimScene() {return simScene;}

}


