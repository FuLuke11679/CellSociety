package cellsociety;

import cellsociety.model.grid.Grid;
import cellsociety.model.ruleset.*;
import cellsociety.parser.XMLParser;
import cellsociety.view.GridView;
import cellsociety.view.GridView.ColorScheme;
import cellsociety.view.SplashScreen;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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
//            int[] values = myParser.getValues
//            if (values != null && myParser.getSimType().equals("Sugarscape")) {
//                ((SugarscapeRuleset) ruleset).setInitialValues(values);
//            }
            myGrid = ruleset.createGrid(myParser.getRows(), myParser.getColumns(), myParser.getInitialStates());
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
        return RulesetFactory.createRuleset(myParser.getSimType(), myParser.getSimVarsMap());
    }

    private void startSimulation() {
        simLoop = new Timeline(new KeyFrame(Duration.seconds(SECOND_DELAY), e -> {
            myGrid.update();
            myGridView.update();
        }));
        simLoop.setCycleCount(Timeline.INDEFINITE);
        simLoop.play();
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
        HBox splashControls = mySplashScreen.getControls();
        if(splashControls.getChildren().size()<3){
            splashControls.getChildren().add(loadButton);
            mySplashScreen.getSplashPane().setBottom(splashControls);
        }
    }

    /**
     * Continuously updates splash screen as user makes customization choices
     */
    private void splashLoop(){
        splashLoop = new Timeline(new KeyFrame(Duration.seconds(0.05), e -> {
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
        HBox centerWrapper = new HBox(myGridView.getScene().getRoot());
        centerWrapper.setAlignment(Pos.CENTER);
        layout.setCenter(centerWrapper);

        Button startButton = new Button(simInfo.getString("start"));
        Button pauseButton = new Button(simInfo.getString("pause"));
        Button saveButton = new Button(simInfo.getString("save"));
        Button resetButton = new Button(simInfo.getString("reset"));
        Button loadButton = new Button(simInfo.getString("load_file"));

        startButton.setOnAction(e -> startSimulation());
        pauseButton.setOnAction(e -> simLoop.stop());
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

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public ResourceBundle getResourceBundle(String name){
        return ResourceBundle.getBundle(name, myLocale);
    }


    public static void main(String[] args) {
        launch(args);
    }
}

