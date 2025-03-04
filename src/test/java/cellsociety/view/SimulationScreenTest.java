package cellsociety.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

public class SimulationScreenTest extends DukeApplicationTest {
  private SimulationController myController;
  private SplashScreen mySplashScreen;
  // keep GUI components used in multiple tests
  private BorderPane myPane;
  private SimulationScreen mySimScreen;
  private Parent root;

  @Override
  public void start (Stage stage) {
    // create application and add scene for testing to given stage
    myController = new SimulationController(stage, Locale.getDefault());
    mySplashScreen = new SplashScreen(myController);
    mySplashScreen.loadScreen();
    File dataFile = new File("/Users/palosilva/Desktop/CS_308/cellsociety_team08/data/segregation/segregation_50x50.xml");
    //need to load a simulation
    myController.loadSimulation(dataFile);
    root = myController.getSimScreen().getSimScene().getRoot();
  }

  @Test
  void buttonClick_start_simStarts () {
    //Button startButton = lookup("#layout #simControls #start").query();
    Button startButton = (Button) root.lookup(".start-button");
    // GIVEN, app first starts up
    // WHEN, language menu is selected and French is chosen
    //writeInputTo(myTextField, expected);
    clickOn(startButton);
    assert(myController.getSimLoop().getStatus() == Animation.Status.RUNNING);
  }

}
