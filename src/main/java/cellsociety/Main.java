package cellsociety;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
    /**
     * Will be determined through reading XML, but for now hard coded*/
    private static final int ROWS = 50;
    private static final int COLUMNS = 50;
    private static final int CELL_SIZE = 16;
    private static final Color ALIVE_COLOR = Color.BLUE;
    private static final Color DEAD_COLOR = Color.WHITE;
    private static final double DENSITY = 0.4;

    /**
     * @see Application#start(Stage)
     */
    @Override
    public void start (Stage primaryStage) {
        /**
         *         showMessage(AlertType.INFORMATION, String.format("Version: %s", getVersion()));
         *         File dataFile = FILE_CHOOSER.showOpenDialog(primaryStage);
         *         if (dataFile != null) {
         *             int numBlocks = calculateNumBlocks(dataFile);
         *             if (numBlocks != 0) {
         *                 showMessage(AlertType.INFORMATION, String.format("Number of Blocks = %d", numBlocks));
         *             }
         *         }
         */
        GridManager gridManager = new GridManager(ROWS, COLUMNS, CELL_SIZE, ALIVE_COLOR, DEAD_COLOR, DENSITY);

        // Initialize the grid
        gridManager.initializeGrid();
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        Text descriptionText = new Text("Simulation Description");
        BorderPane.setAlignment(descriptionText, Pos.CENTER);
        root.setTop(descriptionText);
        HBox controlsPanel = new HBox(10); // Spacing between controls
        controlsPanel.setAlignment(Pos.CENTER);

        // Add ruleset selection combo box
        /**
        ComboBox<String> rulesetComboBox = new ComboBox<>();
        rulesetComboBox.getItems().addAll("Game of Life", "Percolation", "Fire Spread", "Segregation", "Wa-Tor");
        rulesetComboBox.setValue("Game of Life"); // Default selection
        rulesetComboBox.setOnAction(e -> changeRuleset(rulesetComboBox.getValue()));
        */
        // Add speed slider
        /**
        Slider speedSlider = new Slider(1, 10, 5); // Min, Max, Default
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(1);
        speedSlider.setMinorTickCount(0);
        speedSlider.setSnapToTicks(true);
        */
        // Add controls to the controls panel
        /**
        controlsPanel.getChildren().addAll(
            new Label("Ruleset:"), rulesetComboBox,
            new Label("Speed:"), speedSlider
        );
        */

        // Add the controls panel to the top of the root layout
        root.setTop(new VBox(10, descriptionText, controlsPanel)); // Spacing between description and controls

        // Add the grid to the center of the root layout
        root.setCenter(gridManager.getGridPane());

        // Set up the scene and stage
        Scene scene = new Scene(root, COLUMNS * CELL_SIZE, ROWS * CELL_SIZE + 100);
        primaryStage.setTitle("Cell Society");
        primaryStage.setScene(scene);
        primaryStage.show();
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
