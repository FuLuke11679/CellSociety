package cellsociety;

import cellsociety.model.grid.Grid;
import cellsociety.model.ruleset.*;
import cellsociety.parser.XMLParser;
import cellsociety.view.GridView;
import cellsociety.view.GridView.ColorScheme;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
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

public class Main extends Application {
    private static final String DATA_FILE_EXTENSION = "*.xml";
    private static final FileChooser FILE_CHOOSER = new FileChooser();
    private Timeline simLoop;
    private static double SECOND_DELAY = 0.8;
    private static Stage globalStage;
    private GridView myGridView;
    private Grid myGrid;
    private XMLParser myParser;
    private File currentFile;
    private ColorScheme myScheme;

    @Override
    public void start(Stage primaryStage) {
        globalStage = primaryStage;
        loadSplashScreen();
    }

    private void loadSimulation(File dataFile, ColorScheme myScheme) {

        currentFile = dataFile;
        myParser = new XMLParser(dataFile);
        Ruleset ruleset = getRuleset();
        myGrid = ruleset.createGrid(myParser.getRows(), myParser.getColumns(), myParser.getInitialStates());
        myGridView = new GridView(
            myParser.getRows(),
            myParser.getColumns(),
            myParser.getSimType(),
            myParser.getTitle(),
            myParser.getAuthor(),
            myParser.getDescription(),
            myGrid,
            myScheme);

        BorderPane layout = initializeLayout();

        //why is this hardcoded???
        setStage(new Scene(layout, 600, 800));
    }

    private Ruleset getRuleset() {
        return switch (myParser.getSimType()) {
            case "Conway" -> new ConwayRuleset();
            case "Percolation" -> new PercolationRuleset();
            case "Fire" -> new FireRuleset(Double.parseDouble(myParser.getSimVarsMap().get("probCatch")),
                Double.parseDouble(myParser.getSimVarsMap().get("probGrow")));
            case "Segregation" -> new SegregationRuleset(Double.parseDouble(myParser.getSimVarsMap().get("thresh")));
            default -> throw new IllegalStateException("Unknown simulation type");
        };
    }

    private void startSimulation() {
        simLoop = new Timeline(new KeyFrame(Duration.seconds(SECOND_DELAY), e -> {
            myGrid.update();
            myGridView.update();
        }));
        simLoop.setCycleCount(Timeline.INDEFINITE);
        simLoop.play();
    }

    private void loadSplashScreen() {
        BorderPane splash = new BorderPane();
        Text welcome = new Text("""
            \n\n\n\n\n\n\n\n\n\n
            Welcome to Cell Society Simulations
            
            Please select a language and color scheme before loading the simulation.
            """);
        TextFlow textFlow = new TextFlow(welcome);
        textFlow.setTextAlignment(TextAlignment.CENTER);
        splash.setCenter(textFlow);
        Button loadButton = new Button("Load Simulation File");
        MenuButton languageSelect = new MenuButton("Language");
        MenuItem language1 = new MenuItem("English");
        MenuItem language2 = new MenuItem("Spanish");
        MenuItem language3 = new MenuItem("French");
        BooleanProperty languageSelected = new SimpleBooleanProperty(false);
        //code itself can't translate language in description
        //would have to be hardcoded in the CSS/resource property files
        //but then no way to deal with descriptions provided by the user
        language1.setOnAction(e -> languageSelected.set(true));
        language2.setOnAction(e -> languageSelected.set(true));
        language3.setOnAction(e -> languageSelected.set(true));
        languageSelect.getItems().addAll(language1, language2, language3);
        MenuButton colorScheme = new MenuButton("Color Scheme");
        MenuItem colorScheme1 = new MenuItem("Dark Mode");
        MenuItem colorScheme2 = new MenuItem("Light Mode");
        MenuItem colorScheme3 = new MenuItem("Duke Mode");
        MenuItem colorScheme4 = new MenuItem("UNC Mode");
        BooleanProperty colorSelected = new SimpleBooleanProperty(false);
        colorScheme1.setOnAction(e -> {
            colorSelected.set(true);
            myScheme = ColorScheme.DARK;
        });
        colorScheme2.setOnAction(e -> {
            colorSelected.set(true);
            myScheme = ColorScheme.LIGHT;
        });
        colorScheme3.setOnAction(e -> {
            colorSelected.set(true);
            myScheme = ColorScheme.DUKE;
        });
        colorScheme4.setOnAction(e -> {
            colorSelected.set(true);
            myScheme = ColorScheme.UNC;
        });
        colorScheme.getItems().addAll(colorScheme1, colorScheme2, colorScheme3, colorScheme4);
        loadButton.setOnAction(e -> {
            File newFile = FILE_CHOOSER.showOpenDialog(globalStage);
            if (newFile != null & languageSelected.get() & colorSelected.get()) {
                //only should load once other buttons have been selected
                loadSimulation(newFile, myScheme);
            }
        });
        HBox controls = new HBox(10, loadButton, languageSelect, colorScheme);
        splash.setBottom(controls);
        setStage(new Scene(splash, 600, 800));
    }

    private BorderPane initializeLayout() {
        BorderPane layout = new BorderPane();
        layout.setCenter(myGridView.getScene().getRoot());

        Button startButton = new Button("Start");
        Button pauseButton = new Button("Pause");
        Button saveButton = new Button("Save");
        Button resetButton = new Button("Reset");
        Button loadButton = new Button("Load New File");

        startButton.setOnAction(e -> startSimulation());
        pauseButton.setOnAction(e -> simLoop.stop());
        saveButton.setOnAction(e -> saveSimulation());
        resetButton.setOnAction(e -> resetSimulation());
        loadButton.setOnAction(e -> {
            File newFile = FILE_CHOOSER.showOpenDialog(globalStage);
            if (newFile != null) {
                loadSimulation(newFile, myScheme);
            }
        });

        Slider speedSlider = new Slider(0.1, 2.0, SECOND_DELAY);
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            SECOND_DELAY = 2.1 - newVal.doubleValue();
            if (!simLoop.getKeyFrames().isEmpty()) {
                simLoop.stop();
                startSimulation();
            }
        });

        HBox controls = new HBox(10, startButton, pauseButton, saveButton, resetButton, loadButton, new Label("Speed:"), speedSlider);
        layout.setBottom(controls);
        return layout;
    }

    private void saveSimulation() {
        TextInputDialog dialog = new TextInputDialog("Simulation");
        dialog.setHeaderText("Enter simulation metadata (Title, Author, Description)");
        dialog.setContentText("Metadata:");
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
                showMessage("Error saving file.");
            }
        }
    }

    private void resetSimulation() {
        loadSimulation(currentFile, myScheme);
    }

    private void setStage(Scene scene) {
        globalStage.setScene(scene);
        globalStage.show();
    }

    private void showMessage(String message) {
        new Alert(Alert.AlertType.INFORMATION, message).showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

