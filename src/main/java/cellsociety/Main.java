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

    private void loadSimulation(File dataFile) {
        try {
            if (dataFile == null || dataFile.length() == 0) {
                throw new IllegalArgumentException("File is empty or invalid.");
            }

            currentFile = dataFile;
            myParser = new XMLParser(dataFile);
            Ruleset ruleset = getRuleset();
            myGrid = ruleset.createGrid(myParser.getRows(), myParser.getColumns(), myParser.getInitialStates());
            if(myParser.getValues() != null) {
                ruleset.setValues(myParser.getValues());
            }
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
            setStage(new Scene(layout, 600, 800));
        } catch (IllegalArgumentException e) {
            showMessage("Invalid Configuration File: " + e.getMessage());
        } catch (Exception e) {
            showMessage("An error occurred while loading the simulation: " + e.getMessage());
        }
    }

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
                getIntFromParser("sugarGrowBackInterval")
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

    private void loadSplashScreen() {
        BorderPane splash = new BorderPane();
        splashScene = new Scene(splash, 600, 800);
        ResourceBundle simInfo = ResourceBundle.getBundle("SimInfo", myLocale);
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

    private BorderPane loadSplashText(BorderPane splash, ResourceBundle simInfo) {
        Text welcome = new Text(simInfo.getString("splash_welcome"));
        TextFlow textFlow = new TextFlow(welcome);
        textFlow.setTextAlignment(TextAlignment.CENTER);
        splash.setCenter(textFlow);
        return splash;
    }

    private List<MenuButton> loadControlButtons(ResourceBundle simInfo) {
        List<MenuButton> controlButtons = new ArrayList<>();
        MenuButton languageSelect = new MenuButton(simInfo.getString("splash_language_button"));
        MenuItem language1 = new MenuItem(simInfo.getString("splash_language_1"));
        MenuItem language2 = new MenuItem(simInfo.getString("splash_language_2"));
        MenuItem language3 = new MenuItem(simInfo.getString("splash_language_3"));
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
        languageSelect.getItems().addAll(language1, language2, language3);
        MenuButton colorScheme = new MenuButton(simInfo.getString("splash_color_button"));
        MenuItem colorScheme1 = new MenuItem(simInfo.getString("splash_color_scheme_1"));
        MenuItem colorScheme2 = new MenuItem(simInfo.getString("splash_color_scheme_2"));
        MenuItem colorScheme3 = new MenuItem(simInfo.getString("splash_color_scheme_3"));
        MenuItem colorScheme4 = new MenuItem(simInfo.getString("splash_color_scheme_4"));
        colorScheme1.setOnAction(e -> {
            setSplashTheme(splashScene, ColorScheme.DARK); //enum just for switch statememt
            myScheme = ColorScheme.DARK; //this should be eliminated
        });
        colorScheme2.setOnAction(e -> {
            setSplashTheme(splashScene, ColorScheme.LIGHT);
            myScheme = ColorScheme.LIGHT;
        });
        colorScheme3.setOnAction(e -> {
            setSplashTheme(splashScene, ColorScheme.DUKE);
            myScheme = ColorScheme.DUKE;
        });
        colorScheme4.setOnAction(e -> {
            setSplashTheme(splashScene, ColorScheme.UNC);
            myScheme = ColorScheme.UNC;
        });
        colorScheme.getItems().addAll(colorScheme1, colorScheme2, colorScheme3, colorScheme4);
        controlButtons.add(languageSelect);
        controlButtons.add(colorScheme);
        return controlButtons;
    }

    private void setSplashTheme(Scene splashScene, ColorScheme scheme){
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
            System.err.println("Error: Invalid Splash Theme");
        }
        splashScene.getStylesheets().add(resourcePath.toExternalForm());
        setStage(splashScene);
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
        loadSimulation(currentFile);
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

