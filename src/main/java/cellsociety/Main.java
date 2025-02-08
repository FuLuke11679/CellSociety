package cellsociety;

import cellsociety.model.Grid;
import cellsociety.model.ruleset.*;
import cellsociety.parser.XMLParser;
import cellsociety.view.GridView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;
import java.io.StringWriter;
import java.util.Map;

public class Main extends Application {
    private static final String DATA_FILE_EXTENSION = "*.xml";
    private static final FileChooser FILE_CHOOSER = new FileChooser();
    private Timeline simLoop;
    private static double SECOND_DELAY = 0.8;
    private static Stage globalStage;
    private GridView myGridView;
    private Grid myGrid;
    private XMLParser myParser;
    private File initialFile;

    @Override
    public void start(Stage primaryStage) {
        globalStage = primaryStage;
        FILE_CHOOSER.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", DATA_FILE_EXTENSION));
        initialFile = FILE_CHOOSER.showOpenDialog(primaryStage);

        if (initialFile != null) {
            loadSimulation(initialFile);
        }
    }

    private void loadSimulation(File dataFile) {
        myParser = new XMLParser(dataFile);
        myGrid = new Grid(myParser.getRows(), myParser.getColumns(), getRuleset(), myParser.getInitialStates());
        myGridView = new GridView(myParser.getRows(), myParser.getColumns(), myGrid);

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
                loadSimulation(newFile);
            }
        });

        Slider speedSlider = new Slider(0.1, 2.0, SECOND_DELAY);
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            SECOND_DELAY = newVal.doubleValue();
            if (!simLoop.getKeyFrames().isEmpty()) {
                simLoop.stop();
                startSimulation();
            }
        });

        HBox controls = new HBox(10, startButton, pauseButton, saveButton, resetButton, loadButton, new Label("Speed:"), speedSlider);
        layout.setBottom(controls);

        setStage(new Scene(layout, 600, 800));
    }

    private Ruleset getRuleset() {
        return switch (myParser.getSimType()) {
            case "Conway" -> new ConwayRuleset();
            case "Percolation" -> new PercolationRuleset();
            case "Fire" -> new FireRuleset(Double.parseDouble(myParser.getSimVarsMap().get("probCatch")),
                Double.parseDouble(myParser.getSimVarsMap().get("probGrow")));
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
        loadSimulation(initialFile);
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

