package cellsociety;

import cellsociety.model.grid.Grid;
import cellsociety.model.ruleset.*;
import cellsociety.parser.XMLParser;
import cellsociety.view.GridView;
import cellsociety.view.GridView.ColorScheme;
import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
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

    //DUPLICATE VARIABLE, NEED to place in property file if possible
    private final Map<ColorScheme, Color> schemeColors = Map.ofEntries(
        Map.entry(ColorScheme.LIGHT, Color.WHITESMOKE),
        Map.entry(ColorScheme.DARK, Color.GRAY),
        Map.entry(ColorScheme.DUKE, Color.BLUE),
        Map.entry(ColorScheme.UNC, Color.LIGHTBLUE)
    );//required for setting language

    @Override
    public void start(Stage primaryStage) {
        globalStage = primaryStage;
        loadSplashScreen();
    }

    private void loadSimulation(File dataFile) {

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
            //myLocale);

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
        myLocale = Locale.ENGLISH; //default to english
        //set all relevant text in this method, but buttons are set and added to layout here
        //this means we have to get children
        //setSplashText(myLocale)
        ResourceBundle simInfo = ResourceBundle.getBundle("SimInfo", myLocale);
        Text welcome = new Text(simInfo.getString("splash_welcome"));
        TextFlow textFlow = new TextFlow(welcome);
        textFlow.setTextAlignment(TextAlignment.CENTER);
        splash.setCenter(textFlow);
        Button loadButton = new Button(simInfo.getString("splash_load_sim"));
        MenuButton languageSelect = new MenuButton(simInfo.getString("splash_language_button"));
        MenuItem language1 = new MenuItem(simInfo.getString("splash_language_1"));
        MenuItem language2 = new MenuItem(simInfo.getString("splash_language_2"));
        MenuItem language3 = new MenuItem(simInfo.getString("splash_language_3"));
        BooleanProperty languageSelected = new SimpleBooleanProperty(false);
        language1.setOnAction(e -> {
            languageSelected.set(true);
            //change language here for all nodes on splash screen
            //myLocale = Locale.ENGLISH;
        });
        language2.setOnAction(e ->
        {
            languageSelected.set(true);
            myLocale = Locale.FRENCH;
            //simInfo = ResourceBundle.getBundle("SimInfo", myLocale);
        });
        language3.setOnAction(e ->
        {
            languageSelected.set(true);
            myLocale = Locale.GERMAN;
        });
        languageSelect.getItems().addAll(language1, language2, language3);
        MenuButton colorScheme = new MenuButton(simInfo.getString("splash_color_button"));
        MenuItem colorScheme1 = new MenuItem(simInfo.getString("splash_color_scheme_1"));
        MenuItem colorScheme2 = new MenuItem(simInfo.getString("splash_color_scheme_2"));
        MenuItem colorScheme3 = new MenuItem(simInfo.getString("splash_color_scheme_3"));
        MenuItem colorScheme4 = new MenuItem(simInfo.getString("splash_color_scheme_4"));
        BooleanProperty colorSelected = new SimpleBooleanProperty(false);
        colorScheme1.setOnAction(e -> {
            colorSelected.set(true);
            myScheme = ColorScheme.DARK;
            setSplashDarkTheme(splash, myScheme);
            //need to set new scene in each of these cases!!
        });
        colorScheme2.setOnAction(e -> {
            colorSelected.set(true);
            myScheme = ColorScheme.LIGHT;
            //setSplashTheme(splash, myScheme);
        });
        colorScheme3.setOnAction(e -> {
            colorSelected.set(true);
            myScheme = ColorScheme.DUKE;
            //setSplashTheme(splash, myScheme);
        });
        colorScheme4.setOnAction(e -> {
            colorSelected.set(true);
            myScheme = ColorScheme.UNC;
            //setSplashTheme(splash, myScheme);
        });
        colorScheme.getItems().addAll(colorScheme1, colorScheme2, colorScheme3, colorScheme4);
        loadButton.setOnAction(e -> {
            File newFile = FILE_CHOOSER.showOpenDialog(globalStage);
            if (newFile != null & languageSelected.get() & colorSelected.get()) {
                //only should load once other buttons have been selected
                loadSimulation(newFile);
            }
        });
        HBox controls = new HBox(10, loadButton, languageSelect, colorScheme);
        splash.setBottom(controls);
        Scene splashScene = new Scene(splash, 600, 800);
        setStage(splashScene);
    }

    private void setSplashDarkTheme(BorderPane splash, ColorScheme scheme){
        //takes scene object and
        splash.setBackground(new Background(new BackgroundFill(schemeColors.get(scheme), CornerRadii.EMPTY, Insets.EMPTY)));
        //Scene splashScene = new Scene(splash, 600, 800);
        //need to modify splash if we want to use it in manner above
       // URL resourceUrl = getClass().getResource("SplashDark.css");

        // Check if the resource URL is null (file not found)
        /*
        if (resourceUrl == null) {
            System.err.println("Error: SplashDark.css file not found!");
            // You can also handle this scenario by providing a default style or a fallback
            return;  // Exit the method or handle the error as needed
        }

         */
        //splashScene.getStylesheets().add(getClass().getResource("SplashDark.css").toExternalForm());
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

