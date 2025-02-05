package cellsociety;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * Feel free to completely change this code or delete it entirely.
 */
public class Main extends Application {
    // kind of data files to look for
    public static final String DATA_FILE_EXTENSION = "*.xml";
    // default to start in the data folder to make it easy on the user to find
    public static final String DATA_FILE_FOLDER = System.getProperty("user.dir") + "/data";
    // NOTE: make ONE chooser since generally accepted behavior is that it remembers where user left it last
    private final static FileChooser FILE_CHOOSER = makeChooser(DATA_FILE_EXTENSION);
    // internal configuration file
    public static final String INTERNAL_CONFIGURATION = "cellsociety.Version";

    private Timeline simLoop;
    private static double SECOND_DELAY = 0.8;  //this can be varied based on sim speed slider
    private static Stage globalStage;
    private GridView myGridView;
    private Grid myGrid;


    /**
     * @see Application#start(Stage)
     */
    @Override
    public void start (Stage primaryStage) {
        //skip xml loading for now--for now just initialize grid randomly and declare size/color/other
        //variables directly in program
        /*
        showMessage(AlertType.INFORMATION, String.format("Version: %s", getVersion()));
        File dataFile = FILE_CHOOSER.showOpenDialog(primaryStage);
        if (dataFile != null) {
            int numBlocks = calculateNumBlocks(dataFile);
            if (numBlocks != 0) {
                showMessage(AlertType.INFORMATION, String.format("Number of Blocks = %d", numBlocks));
            }
        }
         */
        globalStage = primaryStage;
        simLoop = new Timeline();
        myGrid = new Grid(20, 20);
        myGridView = new GridView(20, 20, myGrid); //parameters to constructor will be parsed from xml file
        //myGridView.update(myGrid.getGrid());
        //check what initial scene looks like (should write this in JUnit test next time
        BorderPane layout = new BorderPane();
        layout.setCenter(myGridView.getScene().getRoot());
        Button startButton = new Button("Start");
        Button pauseButton = new Button("Pause");

        startButton.setOnAction(e -> startSimulation());
        pauseButton.setOnAction(e -> simLoop.stop());

        // ✅ Create Speed Slider
        Slider speedSlider = new Slider(0.1, 2.0, 0.8); // Min speed 0.1s, max 2s per step
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(0.5);
        speedSlider.setBlockIncrement(0.1);
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            SECOND_DELAY = newVal.doubleValue();  // ✅ Update speed dynamically
            if (!simLoop.getKeyFrames().isEmpty()) {
                simLoop.stop();
                startSimulation(); // Restart with the new speed
            }
        });
        HBox controls = new HBox(10, startButton, pauseButton, new Text("Speed: "), speedSlider);
        layout.setBottom(controls);
        setStage(new Scene(layout, 500, 700));

    }
    public void startSimulation(){
        simLoop.stop();
        simLoop = new Timeline(new KeyFrame(Duration.seconds(SECOND_DELAY), e -> step(SECOND_DELAY)));
        simLoop.setCycleCount(Timeline.INDEFINITE);
        simLoop.play();
    }

    public void step(double elapsedTime){
        //executes transition to next generation
        //need to update internal grid in Grid class
        //once its been updated then update visual display
        //for now simply make small change to Grid to see update take place
        myGrid.update(); //list of cell ids that were updated
        myGridView.update(myGrid.getLength());

        //PauseTransition pause = new PauseTransition(Duration.seconds(10));
        //pause.play();


    }


    public static void setStage(Scene scene){
        globalStage.setScene(scene);
        globalStage.show();

    }

    /**
     * Returns number of blocks needed to cover the width and height given in the data file.
     */
    public int calculateNumBlocks(File xmlFile) {
        try {
            Document xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
            Element root = xmlDocument.getDocumentElement();
            int width = Integer.parseInt(getTextValue(root, "width"));
            int height = Integer.parseInt(getTextValue(root, "height"));
            return width * height;
        }
        catch (NumberFormatException e) {
            showMessage(AlertType.ERROR, "Invalid number given in data");
            return 0;
        }
        catch (ParserConfigurationException e) {
            showMessage(AlertType.ERROR, "Invalid XML Configuration");
            return 0;
        }
        catch (SAXException | IOException e) {
            showMessage(AlertType.ERROR, "Invalid XML Data");
            return 0;
        }
    }

    /**
     * A method to test getting internal resources.
     */
    public double getVersion () {
        ResourceBundle resources = ResourceBundle.getBundle(INTERNAL_CONFIGURATION);
        return Double.parseDouble(resources.getString("Version"));
    }

    // get value of Element's text
    private String getTextValue (Element e, String tagName) {
        NodeList nodeList = e.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        else {
            // FIXME: empty string or exception? In some cases it may be an error to not find any text
            return "";
        }
    }

    // display given message to user using the given type of Alert dialog box
    void showMessage (AlertType type, String message) {
        new Alert(type, message).showAndWait();
    }

    // set some sensible defaults when the FileChooser is created
    private static FileChooser makeChooser (String extensionAccepted) {
        FileChooser result = new FileChooser();
        result.setTitle("Open Data File");
        // pick a reasonable place to start searching for files
        result.setInitialDirectory(new File(DATA_FILE_FOLDER));
        result.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Data Files", extensionAccepted));
        return result;
    }

    /**
     * Start the program, give complete control to JavaFX.
     *
     * Default version of main() is actually included within JavaFX, so this is not technically necessary!
     */
    public static void main (String[] args) {
        launch(args);
    }
}
