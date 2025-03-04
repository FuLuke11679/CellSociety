package cellsociety;

import cellsociety.model.grid.CellShapeFactory;
import cellsociety.model.grid.EdgeFactory;
import cellsociety.model.grid.Grid;
import cellsociety.model.grid.NeighborhoodFactory;
import cellsociety.model.ruleset.*;
import cellsociety.parser.XMLParser;
import cellsociety.view.GridView;
import cellsociety.view.GridView.ColorScheme;
import cellsociety.view.SimulationController;
import cellsociety.view.SplashScreen;
import cellsociety.view.shapes.ShapeFactory;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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

    private SimulationController myController;


    @Override
    public void start(Stage primaryStage) {
        myLocale = Locale.getDefault();
        myController = new SimulationController(primaryStage, myLocale);
        mySplashScreen = new SplashScreen(myController);
        mySplashScreen.loadScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

