package cellsociety;

import cellsociety.model.grid.CellShapeFactory;
import cellsociety.model.grid.EdgeFactory;
import cellsociety.model.grid.Grid;
import cellsociety.model.grid.NeighborhoodFactory;
import cellsociety.model.ruleset.*;
import cellsociety.parser.XMLParser;
import cellsociety.view.GridView;
import cellsociety.view.GridView.ColorScheme;
import cellsociety.view.SplashScreen;
import cellsociety.view.shapes.ShapeFactory;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;

/**
 * Main class to drive simulations. Extends the Application class of javafx,
 */
public class Main extends Application {
    private static final String DATA_FILE_EXTENSION = "*.xml";
    private static final FileChooser FILE_CHOOSER = new FileChooser();
    private Timeline simLoop;
    private Timeline splashLoop;
    private static double SECOND_DELAY = 0.8;
    private static Stage globalStage;
    private GridView myGridView;
    private Grid myGrid;
    private XMLParser myParser;
    private File currentFile;
    private ColorScheme myScheme;
    private Locale myLocale;
    private SplashScreen mySplashScreen;
    private int width = 800;
    private int height = 800;


    @Override
    public void start(Stage primaryStage) {
        globalStage = primaryStage;
        myLocale = Locale.getDefault(); //default should be English
        loadSplashScreen();
    }

    /**
     * Function to load a new simulation
     * @param dataFile : XML File chosen by user from their local machine
     */
    private void loadSimulation(File dataFile) {
        //ResourceBundle simInfo = getResourceBundle("SimInfo");
        ResourceBundle simInfo = ResourceBundle.getBundle("SimInfo", myLocale);
        splashLoop.stop();
        try {
            if (dataFile == null || dataFile.length() == 0) {
                throw new IllegalArgumentException(simInfo.getString("invalid_file"));
            }

            currentFile = dataFile;
            myParser = new XMLParser(dataFile);
            Ruleset ruleset = getRuleset();
            int[] values = myParser.getValues();
            if (values != null && myParser.getSimType().equals("Sugarscape")) {
                ((SugarscapeRuleset) ruleset).setInitialValues(values);
            }
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
                myScheme,
                myLocale);

            BorderPane layout = initializeLayout(simInfo);
            setStage(new Scene(layout, width, height));
        } catch (IllegalArgumentException e) {
            showMessage(simInfo.getString("invalid_config") + e.getMessage());
        } catch (Exception e) {
            showMessage(simInfo.getString("load_error") + e.getMessage());
        }
    }

    /**
     * Creates new instance of a ruleset for corresponding simulation
     * @return Ruleset for loaded simulation
     */

    private Ruleset getRuleset() {
        return switch (myParser.getSimType()) {
            case "Conway" -> new ConwayRuleset();
            case "Percolation" -> new PercolationRuleset();
            case "Fire" -> new FireRuleset(getDoubleFromParser("probCatch"), getDoubleFromParser("probGrow"));
            case "Segregation" -> new SegregationRuleset(getDoubleFromParser("thresh"));
            case "WatorWorld" -> new WatorRuleset(
                getIntFromParser("fishBreedTime"),
                getIntFromParser("fishStarveTime"),
                getIntFromParser("sharkBreedTime"),
                getIntFromParser("sharkStarveTime")
            );
            case "GeneralConway" -> new GeneralConwayRuleset(myParser.getSimVarsMap().get("rules"));
            case "Sugarscape" -> new SugarscapeRuleset(
                getIntFromParser("sugarGrowBackRate"),
                getIntFromParser("sugarGrowBackInterval"),
                getIntFromParser("agentVision"),
                getIntFromParser("agentMetabolism")
            );
            default -> throw new IllegalStateException("Unknown simulation type: " + myParser.getSimType());
        };
    }

    private int getIntFromParser(String fieldKey) {
        return Integer.parseInt(myParser.getSimVarsMap().get(fieldKey));
    }

    private double getDoubleFromParser(String fieldKey) {
        return Double.parseDouble(myParser.getSimVarsMap().get(fieldKey));
    }

    private void startSimulation() {
        if (simLoop == null) {
            simLoop = new Timeline(new KeyFrame(Duration.seconds(SECOND_DELAY), e -> {
                myGrid.update();
                myGridView.update();
            }));
            simLoop.setCycleCount(Timeline.INDEFINITE);
            simLoop.setRate(1.0 / SECOND_DELAY);  // Ensure speed is set correctly
        }
        if (simLoop.getStatus() == Animation.Status.PAUSED) {
            simLoop.play();  // Resume if paused
        } else if (simLoop.getStatus() != Animation.Status.RUNNING) {
            simLoop.playFromStart();  // Start fresh if it wasn't running
        }
    }


    /**
     * Loads opening screen, providing user with customization choices
     */
    private void loadSplashScreen(){
        mySplashScreen = new SplashScreen(myLocale);
        setLoadButton();
        splashLoop();
    }

    /**
     * Generates load button to start selected simulation, labeled with the correct language
     */
    private void setLoadButton(){
        ResourceBundle simInfo = getResourceBundle("SimInfo");
        Button loadButton = new Button(simInfo.getString("splash_load_sim"));
        loadButton.setOnAction(e -> {
            File newFile = FILE_CHOOSER.showOpenDialog(globalStage);
            if (newFile != null) {
                loadSimulation(newFile);
            }
        });
        mySplashScreen.getSplashPane().setRight(loadButton);
    }

    /**
     * Continuously updates splash screen as user makes customization choices
     */
    private void splashLoop(){
        splashLoop = new Timeline(new KeyFrame(Duration.seconds(SECOND_DELAY), e -> {
            myLocale = mySplashScreen.getMyLocale();
            myScheme = mySplashScreen.getColorScheme();
            setLoadButton();
            setStage(mySplashScreen.getSplashScene());

        }));
        splashLoop.setCycleCount(Timeline.INDEFINITE);
        splashLoop.play();
    }

    /**
     * Initializes simulation layout after splash screen
     * @param simInfo: resource bundle containing hardcoded simulation text
     * @return : Organized BorderPane holding nodes for simulation
     */
    private BorderPane initializeLayout(ResourceBundle simInfo) {
        BorderPane layout = new BorderPane();
        layout.setCenter(myGridView.getScene().getRoot());

        Button startButton = new Button(simInfo.getString("start"));
        Button pauseButton = new Button(simInfo.getString("pause"));
        Button saveButton = new Button(simInfo.getString("save"));
        Button resetButton = new Button(simInfo.getString("reset"));
        Button loadButton = new Button(simInfo.getString("load_file"));

        startButton.setOnAction(e -> startSimulation());
        pauseButton.setOnAction(e -> {
            if (simLoop != null) {
                if (simLoop.getStatus() == Animation.Status.RUNNING) {
                    simLoop.pause();  // Pause without destroying it
                } else if (simLoop.getStatus() == Animation.Status.PAUSED) {
                    simLoop.play();  // Resume from where it left off
                }
            }
        });

        saveButton.setOnAction(e -> saveSimulation(simInfo));
        resetButton.setOnAction(e -> resetSimulation());
        loadButton.setOnAction(e -> {
            File newFile = FILE_CHOOSER.showOpenDialog(globalStage);
            if (newFile != null) {
                loadSimulation(newFile);
            }
        });

        Slider speedSlider = new Slider(0.1, 2.0, SECOND_DELAY);
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            SECOND_DELAY = 2.1 - newVal.doubleValue();  // Update speed delay

            if (simLoop != null) {
                simLoop.setRate(1.0 / SECOND_DELAY);  // Adjust the playback speed
            }
        });

        HBox controls = new HBox(10, startButton, pauseButton, saveButton, resetButton, loadButton, new Label("Speed:"), speedSlider);
        layout.setBottom(controls);

        VBox settingsPanel = new VBox(15);
        settingsPanel.setStyle("-fx-padding: 10;");

        Label settingsTitle = new Label("Grid Settings");

// Edge Dropdown
        ComboBox<String> edgeDropdown = new ComboBox<>();
        edgeDropdown.getItems().addAll("toroidal", "mirror", "infinite");
        edgeDropdown.setValue(myParser.getEdgeType());
        edgeDropdown.setOnAction(e -> {
            myGrid.setEdgeHandler(EdgeFactory.createEdgeHandler(edgeDropdown.getValue()));
        });

// Neighborhood Dropdown
        ComboBox<String> neighborhoodDropdown = new ComboBox<>();
        neighborhoodDropdown.getItems().addAll("vonNeumann", "extendedMoore");
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

    /**
     * Saves simulation to an xml file
     * @param simInfo resource bundle containing hardcoded simulation text
     */
    private void saveSimulation(ResourceBundle simInfo) {
        TextInputDialog dialog = new TextInputDialog(simInfo.getString("sim"));
        dialog.setHeaderText(simInfo.getString("prompt"));
        dialog.setContentText(simInfo.getString("metadata"));
        dialog.showAndWait();

        File saveFile = FILE_CHOOSER.showSaveDialog(globalStage);
        if (saveFile != null) {
            try {
                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                Element root = doc.createElement("Simulation");
                root.setAttribute("Title", dialog.getEditor().getText());
                doc.appendChild(root);

                Element grid = doc.createElement("Grid");
                grid.setAttribute("rows", String.valueOf(myParser.getRows()));
                grid.setAttribute("columns", String.valueOf(myParser.getColumns()));
                root.appendChild(grid);

                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(saveFile);
                transformer.transform(source, result);
            } catch (Exception e) {
                showMessage(simInfo.getString("save_error"));
            }
        }
    }

    private void resetSimulation() {
        loadSimulation(currentFile);
    }

    private void setStage(Scene scene) {
        globalStage.setScene(scene);
        globalStage.show();
    }

    private void showMessage(String message) {
        new Alert(Alert.AlertType.INFORMATION, message).showAndWait();
    }

    public ResourceBundle getResourceBundle(String name){
        return ResourceBundle.getBundle(name, myLocale);
    }


    public static void main(String[] args) {
        launch(args);
    }
}

