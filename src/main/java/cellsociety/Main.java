package cellsociety;

import cellsociety.model.grid.Grid;
import cellsociety.model.ruleset.*;
import cellsociety.parser.XMLParser;
import cellsociety.view.GridView;
import cellsociety.view.GridView.ColorScheme;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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

/**
 * Main class to drive simulations. Extends the Application class of javafx,
 */
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
    private Locale myLocale;
    private Scene splashScene;


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
        ResourceBundle simInfo = getResourceBundle("SimInfo");
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
            setStage(new Scene(layout, 600, 800));
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

    private void loadSplashScreen() {
        BorderPane splash = new BorderPane();
        splashScene = new Scene(splash, 600, 800);
        ResourceBundle simInfo = getResourceBundle("SimInfo");
        splash = loadSplashText(splash, simInfo);  //returns BorderPane
        Button loadButton = new Button(simInfo.getString("splash_load_sim"));
        loadButton.setOnAction(e -> {
            File newFile = FILE_CHOOSER.showOpenDialog(globalStage);
            if (newFile != null) {
              loadSimulation(newFile);
            }
        });
        List<MenuButton> controlButtons = loadControlButtons(simInfo);
        HBox controls = new HBox(10, loadButton);
        for (MenuButton controlButton : controlButtons) {
            controls.getChildren().add(controlButton);
        }
        splash.setBottom(controls);
        setStage(splashScene);
    }

    /**
     * Generates text on opening screen
     * @param splash : BorderPane object we are building for splash screen
     * @param simInfo : resource bundle containing hardcoded simulation text
     * @return: BorderPane with organized text nodes
     */
    private BorderPane loadSplashText(BorderPane splash, ResourceBundle simInfo) {
        Text welcome = new Text(simInfo.getString("splash_welcome"));
        TextFlow textFlow = new TextFlow(welcome);
        textFlow.setTextAlignment(TextAlignment.CENTER);
        splash.setCenter(textFlow);
        return splash;
    }

    /**
     * Generates control customization buttons for openining screen
     * @param simInfo: resource bundle containing hardcoded simulation text
     * @return List of Buttons
     */

    private List<MenuButton> loadControlButtons(ResourceBundle simInfo) {
        List<MenuButton> controlButtons = new ArrayList<>();
        MenuButton languageSelect = new MenuButton(simInfo.getString("splash_language_button"));
        MenuItem language1 = new MenuItem(simInfo.getString("splash_language_1"));
        MenuItem language2 = new MenuItem(simInfo.getString("splash_language_2"));
        MenuItem language3 = new MenuItem(simInfo.getString("splash_language_3"));
        MenuItem language4 = new MenuItem(simInfo.getString("splash_language_4"));
        language1.setOnAction(e -> {
            if(myLocale != Locale.ENGLISH) {
                myLocale = Locale.ENGLISH;
                loadSplashScreen();  //need to reload if language has been changed
            }
        });
        language2.setOnAction(e -> {
            if(myLocale != Locale.FRENCH) {
                myLocale = Locale.FRENCH;
                loadSplashScreen();
            }
        });
        language3.setOnAction(e ->{
            if(myLocale != Locale.GERMAN){
                myLocale = Locale.GERMAN;
                loadSplashScreen();
            }
        });
        language4.setOnAction(e ->{
            if(myLocale != Locale.ITALIAN){
                myLocale = Locale.ITALIAN;
                loadSplashScreen();
            }
        });
        languageSelect.getItems().addAll(language1, language2, language3, language4);
        MenuButton colorScheme = new MenuButton(simInfo.getString("splash_color_button"));
        MenuItem colorScheme1 = new MenuItem(simInfo.getString("splash_color_scheme_1"));
        MenuItem colorScheme2 = new MenuItem(simInfo.getString("splash_color_scheme_2"));
        MenuItem colorScheme3 = new MenuItem(simInfo.getString("splash_color_scheme_3"));
        MenuItem colorScheme4 = new MenuItem(simInfo.getString("splash_color_scheme_4"));
        colorScheme1.setOnAction(e -> {
            setSplashTheme(splashScene, ColorScheme.DARK, simInfo); //enum just for switch statememt
            myScheme = ColorScheme.DARK; //this should be eliminated
        });
        colorScheme2.setOnAction(e -> {
            setSplashTheme(splashScene, ColorScheme.LIGHT, simInfo);
            myScheme = ColorScheme.LIGHT;
        });
        colorScheme3.setOnAction(e -> {
            setSplashTheme(splashScene, ColorScheme.DUKE, simInfo);
            myScheme = ColorScheme.DUKE;
        });
        colorScheme4.setOnAction(e -> {
            setSplashTheme(splashScene, ColorScheme.UNC, simInfo);
            myScheme = ColorScheme.UNC;
        });
        colorScheme.getItems().addAll(colorScheme1, colorScheme2, colorScheme3, colorScheme4);
        controlButtons.add(languageSelect);
        controlButtons.add(colorScheme);
        return controlButtons;
    }

    /**
     * Sets theme of simulation
     * @param splashScene : current scene we are modifying
     * @param scheme : color scheme
     * @param simInfo: resource bundle containing hardcoded simulation text
     */
    private void setSplashTheme(Scene splashScene, ColorScheme scheme, ResourceBundle simInfo){
        URL resourcePath = null;
        switch(scheme){
            case DARK:
                resourcePath = getClass().getResource("/SplashDark.css");
                break;
            case LIGHT:
                resourcePath = getClass().getResource("/SplashLight.css");
                break;
            case DUKE:
                resourcePath = getClass().getResource("/SplashDuke.css");
                break;
            case UNC:
                resourcePath = getClass().getResource("/SplashUnc.css");
                break;
        }

        if (resourcePath == null) {
            System.err.println(simInfo.getString("invalid_theme"));
        }
        splashScene.getStylesheets().add(resourcePath.toExternalForm());
        setStage(splashScene);
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

