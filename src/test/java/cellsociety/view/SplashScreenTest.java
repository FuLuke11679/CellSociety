package cellsociety.view;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;
import static org.junit.jupiter.api.Assertions.*;
import util.DukeApplicationTest;

public class SplashScreenTest extends DukeApplicationTest {
  // keep only if needed to call application methods in tests
  private SimulationController myController;
  private SplashScreen mySplashScreen;
  // keep GUI components used in multiple tests
  private BorderPane myPane;

  @Override
  public void start (Stage stage) {
    // create application and add scene for testing to given stage
    myController = new SimulationController(stage, Locale.getDefault());
    mySplashScreen = new SplashScreen(myController);
    mySplashScreen.loadScreen();

    // components, found using their IDs, that will be reused in different tests
    myPane = lookup("#splashPane").query();

  }

  @Test
  void buttonClick_languageSelectFrench_languageChanged () {
    ResourceBundle simInfo = ResourceBundle.getBundle("SimInfo", Locale.FRENCH);
    MenuButton frenchLanguageButton = lookup("#splashPane #controls #language #french").query();
    String expected = simInfo.getString("splash_welcome");
    // GIVEN, app first starts up
    // WHEN, language menu is selected and French is chosen
    //writeInputTo(myTextField, expected);
    clickOn(frenchLanguageButton);
    Text resultNode = lookup("#splash-pane #welcome").query();
    String result = resultNode.getText();
    // THEN, check label text has been updated to match input
    assertEquals(expected, result);
  }


}
