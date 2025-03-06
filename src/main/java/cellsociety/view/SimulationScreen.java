package cellsociety.view;

import cellsociety.model.factory.RulesetFactory;
import cellsociety.model.grid.CellShapeFactory;
import cellsociety.model.grid.EdgeFactory;
import cellsociety.model.grid.Grid;
import cellsociety.model.grid.NeighborhoodFactory;
import cellsociety.model.ruleset.Ruleset;
import cellsociety.model.ruleset.SugarscapeRuleset;
import cellsociety.parser.XMLParser;
import cellsociety.view.GridView.ColorScheme;
import cellsociety.view.shapes.ShapeFactory;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

/**
 * @Author Palo Silva Class that handles front end logic for displaying a selected simulation
 */
public class SimulationScreen {

  private final SimulationController myController;
  private XMLParser myParser;
  private GridView myGridView;
  private Grid myGrid;
  private Scene simScene;
  private final int width = 800;
  private final int height = 800;
  private static double SECOND_DELAY = 0.8;

  public SimulationScreen(File file, SimulationController controller) {
    myController = controller;
    parseFile(file);
  }

  /**
   * Parses XML file in order to load simulation
   *
   * @param file: XML file chosen by user
   */

  private void parseFile(File file) {
    ResourceBundle simInfo = ResourceBundle.getBundle("SimInfo", myController.getLocale());
    try {
      if (file == null || file.length() == 0) {
        throw new IllegalArgumentException(simInfo.getString("invalid_file"));
      }

      myParser = new XMLParser(file);
      Ruleset ruleset = getRuleset();
      int[] values = myParser.getValues();
      if (values != null && myParser.getSimType().equals("Sugarscape")) {
        ((SugarscapeRuleset) ruleset).setInitialValues(values);
      }
      myGrid = ruleset.createGrid(myParser.getRows(), myParser.getColumns(),
          myParser.getInitialStates());
      myGrid.setEdgeHandler(EdgeFactory.createEdgeHandler(myParser.getEdgeType()));
      myGrid.setNeighborhoodStrategy(
          NeighborhoodFactory.createNeighborhoodStrategy(myParser.getNeighborhoodType()));
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
      setSimTheme(myController.getScheme(), simInfo);
      myController.setStage(simScene);
    } catch (IllegalArgumentException e) {
      myController.showMessage(simInfo.getString("invalid_config") + e.getMessage());
    } catch (Exception e) {
      myController.showMessage(simInfo.getString("load_error") + e.getMessage());
    }
  }

  /**
   * Generates appropriate RuleSet based on XML file
   *
   * @return RuleSet for chosen simulation
   */
  private Ruleset getRuleset() {
    return RulesetFactory.createRuleset(myParser.getSimType(), myParser.getSimVarsMap());
  }

  /**
   * Initializes simulation layout
   *
   * @param simInfo: resource bundle containing hardcoded simulation text
   * @return : Organized BorderPane holding nodes for simulation
   */
  private BorderPane initializeLayout(ResourceBundle simInfo) {
    BorderPane layout = new BorderPane();
    HBox centerWrapper = new HBox(myGridView.getScene().getRoot());
    centerWrapper.setAlignment(Pos.CENTER);
    centerWrapper.getStyleClass().add("grid");
    layout.setCenter(centerWrapper);

    List<Button> controlButtons = loadControlButtons(simInfo);

    Slider speedSlider = makeSpeedControl();

    HBox controls = new HBox(10);
    for (Button controlButton : controlButtons) {
      controls.getChildren().add(controlButton);
    }
    controls.getChildren().add(new Label("Speed:"));
    controls.getChildren().add(speedSlider);
    layout.setBottom(controls);

    VBox settingsPanel = loadSettingsPanel();
    layout.setRight(settingsPanel);
    return layout;
  }

  /**
   * Loads all control buttons for the simulation display
   *
   * @param simInfo: resource bundle containing hardcoded simulation text
   * @return : List of all control buttons
   */
  private List<Button> loadControlButtons(ResourceBundle simInfo) {
    List<Button> controlButtons = new ArrayList<>();
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
    controlButtons.add(startButton);
    pauseButton.setOnAction(e -> {
      if (myController.getSimLoop() != null) {
        if (myController.getSimLoop().getStatus() == Animation.Status.RUNNING) {
          myController.getSimLoop().pause();  // Pause without destroying it
        } else if (myController.getSimLoop().getStatus() == Animation.Status.PAUSED) {  //REMOVE?
          myController.getSimLoop().play();  // Resume from where it left off
        }
      }
    });
    controlButtons.add(pauseButton);
    saveButton.setOnAction(e -> myController.saveSimulation(simInfo));
    controlButtons.add(saveButton);
    resetButton.setOnAction(e -> myController.resetSimulation());
    controlButtons.add(resetButton);
    loadButton.setOnAction(e -> {
      File newFile = myController.getFileChooser().showOpenDialog(myController.getStage());
      if (newFile != null) {
        //myController.getSimLoop().stop(); //NEW
        myController.loadSimulation(newFile);
      }
    });
    controlButtons.add(loadButton);
    return controlButtons;
  }

  /**
   * Creates slider to adjust simulation speed
   *
   * @return Slider object that adjusts speed
   */
  private Slider makeSpeedControl() {
    Slider speedSlider = new Slider(0.1, 2.0, SECOND_DELAY);
    speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
      SECOND_DELAY = 2.1 - newVal.doubleValue();  // Update speed delay

      if (myController.getSimLoop() != null) {
        myController.getSimLoop().setRate(1.0 / SECOND_DELAY);  // Adjust the playback speed
      }
    });
    speedSlider.getStyleClass().add("speed-slider");
    return speedSlider;
  }

  /**
   * Creates UI elements to adjust edge, neighbor, and shape settings of simulation
   *
   * @return VBox containing all setting UI elements
   */
  private VBox loadSettingsPanel() {
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
      myGrid.setNeighborhoodStrategy(
          NeighborhoodFactory.createNeighborhoodStrategy(neighborhoodDropdown.getValue()));
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
    return settingsPanel;
  }

  /**
   * Sets Color Theme of simulation screen
   *
   * @param scheme  Color scheme given by controller
   * @param simInfo Resource bundle
   */
  private void setSimTheme(ColorScheme scheme, ResourceBundle simInfo) {
    URL resourcePath = switch (scheme) {
      case DARK -> getClass().getResource("/SplashDark.css");
      case LIGHT -> getClass().getResource("/SplashLight.css");
      case DUKE -> getClass().getResource("/SplashDuke.css");
      case UNC -> getClass().getResource("/SplashUnc.css");
    };

    if (resourcePath == null) {
      System.err.println(simInfo.getString("invalid_theme"));
    }
    simScene.getStylesheets().clear();
    simScene.getStylesheets().add(resourcePath.toExternalForm());
  }

  /**
   * Updates simulation screen by updating Grid, then GridView
   */
  public void update() {
    myGrid.update();
    myGridView.update();
  }

  public XMLParser getMyParser() {
    return myParser;
  }

  public Scene getSimScene() {
    return simScene;
  }

}


