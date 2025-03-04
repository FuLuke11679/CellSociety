package cellsociety.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testfx.util.WaitForAsyncUtils.waitFor;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
  private Button startButton;
  private BorderPane initLayout;
  private HBox initGrid;

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
    initLayout = (BorderPane) root.lookup(".layout");
    initGrid = (HBox) root.lookup(".grid");
    startButton = (Button) root.lookup(".start-button");
  }

  @Test
  void buttonClick_start_simStarts () {
    // GIVEN, app first starts up
    // WHEN, start button is clicked, sim loop begins
    clickOn(startButton);
    assert(myController.getSimLoop().getStatus() == Animation.Status.RUNNING);
  }

  @Test
  void buttonClick_pause_simPauses(){
    Button pauseButton = (Button) root.lookup(".pause-button");
    clickOn(startButton);  //start simulation
    //GIVEN, simulation is running
    //WHEN, pause button is clicked, simulation pauses
    clickOn(pauseButton);
    assert(myController.getSimLoop().getStatus() == Animation.Status.PAUSED);
  }

  @Test
  void buttonClick_reset_simResets(){
    Button resetButton = (Button) root.lookup(".reset-button");
    clickOn(startButton);
    //GIVEN, simulation is running
    //WHEN, reset button is clicked, simulation resets
    clickOn(resetButton);
    assert(myController.getSimLoop().getStatus() != Animation.Status.RUNNING);
  }

  @Test
  void buttonClick_save_simSaves(){
    Button saveButton = (Button) root.lookup(".save-button");
    clickOn(startButton);
    //GIVEN, simulation is running
    //WHEN, save button is clicked, simulation is stopped and simulation save window pops up
    clickOn(saveButton);
    assert(myController.getSimLoop().getStatus() != Animation.Status.RUNNING);
  }

  @Test
  void buttonClick_loadNewSim_newSimLoaded(){
    File switchDataFile = new File("/Users/palosilva/Desktop/CS_308/cellsociety_team08/data/segregation/Segregation1.xml");
    clickOn(startButton);
    //GIVEN, simulation is running
    //WHEN, load sim button is clicked and new data file is chosen, we render new simulation
    //Use Platform.runLater() to ensure we stay in javafx thread
    Platform.runLater(() -> {
      myController.loadSimulation(switchDataFile);

    });
    try {
      Thread.sleep(1000);  // Wait for the UI to update
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    assert (myController.getCurrentFile() == switchDataFile);

    //Grids will technically have same address in mem, are considered the same upon comparison
    //HBox newGrid = (HBox) root.lookup(".grid");
    //assert(newGrid != initGrid);  // The layout should have changed
  }

  @Test
  void speedSlider_increaseSpeed_speedIncreases(){
    Slider speedSlider = (Slider) root.lookup(".speed-slider");
    clickOn(startButton);
    //GIVEN, simulation is running
    //WHEN, speed slider is increased, speed of simulation increases
    setValue(speedSlider, 1.9);
    double newSpeed = (1.0)/(2.1-1.9); //how new speed is set in speedSlider
    assertEquals(newSpeed, myController.getSimLoop().getRate(), 0.01);
  }

}
