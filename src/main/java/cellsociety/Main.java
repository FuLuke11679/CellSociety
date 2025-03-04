package cellsociety;

import cellsociety.model.grid.Grid;
import cellsociety.parser.XMLParser;
import cellsociety.view.GridView;
import cellsociety.view.GridView.ColorScheme;
import cellsociety.view.SimulationController;
import cellsociety.view.SplashScreen;
import java.util.Locale;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

/**
 * Main class to drive simulations. Extends the Application class of javafx,
 */
public class Main extends Application { ;
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

