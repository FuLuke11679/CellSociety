package cellsociety;

import cellsociety.view.SimulationController;
import cellsociety.view.SplashScreen;
import java.util.Locale;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @Author Palo Silva Main class to drive simulations. Extends the Application class of javafx,
 */
public class Main extends Application {

  ;
  private Locale myLocale;
  private SplashScreen mySplashScreen;
  private SimulationController myController;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    myLocale = Locale.getDefault();
    myController = new SimulationController(primaryStage, myLocale);
    mySplashScreen = new SplashScreen(myController);
    mySplashScreen.loadScreen();
  }
}

