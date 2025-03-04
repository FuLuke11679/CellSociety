package cellsociety.view;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;


import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.isVisible;

import util.DukeApplicationTest;

public class SplashScreenTest extends DukeApplicationTest {
  // keep only if needed to call application methods in tests
  private SimulationController myController;
  private SplashScreen mySplashScreen;
  // keep GUI components used in multiple tests
  private BorderPane myPane;
  private Parent root;
  private File dataFile;

  @Override
  public void start (Stage stage) {
    // create application and add scene for testing to given stage
    myController = new SimulationController(stage, Locale.getDefault());
    mySplashScreen = new SplashScreen(myController);
    mySplashScreen.loadScreen();

    root = mySplashScreen.getSplashScene().getRoot();
    dataFile = new File("/Users/palosilva/Desktop/CS_308/cellsociety_team08/data/segregation/segregation_50x50.xml");
    // components, found using their IDs, that will be reused in different tests
    //myPane = (BorderPane)root.lookup("#splashPane");
    //myPane = lookup(".splash-pane").query();

  }
  @Test
  void testWelcomeVisible(){
    Text welcome = (Text) root.lookup(".text-welcome");
    verifyThat(welcome, isVisible());
  }

  /*
  @Test
  void testLoadButtonStartsSimulation(){
    Button load = (Button) root.lookup(".load-button");
    clickOn(load);
    verifyThat(myController.getFileChooser().showOpenDialog(myController.getStage()), dataFile);
    //myController.loadSimulation(dataFile);
    //BorderPane simLayout = (BorderPane) mySplashScreen.getSplashScene().getRoot();
    //verifyThat()
  }

   */



  //TODO: Figure out how to deal with MenuButtons
  @Test
  void buttonClick_languageSelectFrench_languageChanged () {
    ResourceBundle simInfo = ResourceBundle.getBundle("SimInfo", Locale.FRENCH);
    //MenuItem frenchButton = (MenuItem) root.lookup(".french-button");
    /*
    Node frenchNode = null;
    Platform.runLater(() -> {
      frenchNode = lookup("#splashPane #controls #language #french").query();
      System.out.println(frenchNode);
    });


    MenuButton frenchLanguageButton = (MenuButton) frenchNode;

     */
    //MenuButton frenchLanguageButton = lookup("#splashPane #controls #language #french").query();
    //MenuButton frenchLanguageButton = (MenuButton)root.lookup("#splashPane #controls #language #french");
    String expected = simInfo.getString("splash_welcome");
    // GIVEN, app first starts up
    // WHEN, language menu is selected and French is chosen
    //writeInputTo(myTextField, expected);
    //clickOn(frenchLanguageButton);
    //Text resultNode = lookup("#splash-pane #welcome").query();
    //String result = resultNode.getText();
    // THEN, check label text has been updated to match input
    //assertEquals(expected, result);
  }



}
