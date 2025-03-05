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
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;


import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.isVisible;
import static org.testfx.util.WaitForAsyncUtils.waitFor;

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
    //GIVEN, Splash Screen is loaded
    //WHEN displayed, welcome text is visible
    Text welcome = (Text) root.lookup(".text-welcome");
    verifyThat(welcome, isVisible());
  }

  @Test
  void testLoadButtonVisible(){
    //GIVEN, Splash Screen is loaded
    //WHEN displayed, load button is visible
    Button load = (Button) root.lookup(".load-button");
    verifyThat(load, isVisible());
  }

  @Test
  void testLanguageButtonVisible(){
    //GIVEN, Splash Screen is loaded
    //WHEN displayed, language button is visible
    MenuButton language = (MenuButton) root.lookup(".language-select");
    verifyThat(language, isVisible());
  }

  @Test
  void testColorThemeButtonVisible(){
    //GIVEN, Splash Screen is loaded
    //WHEN displayed, color theme button is visible
    MenuButton colorTheme = (MenuButton) root.lookup(".color-scheme");
    verifyThat(colorTheme, isVisible());
  }


  @Test
  void buttonClick_languageSelectFrench_languageChanged () {
    ResourceBundle simInfo = ResourceBundle.getBundle("SimInfo", Locale.FRENCH);
    MenuButton languageSelect = (MenuButton) root.lookup(".language-select"); //not a button though
    //GIVEN, splash screen is loaded
    //WHEN, frenchLanguage is selected, language switches to french
    clickOn(languageSelect);
    MenuItem frenchOption = null;
    for (MenuItem item : languageSelect.getItems()) {
      if (item.getText().equals("French")) {
        frenchOption = item;
        break;
      }
    }
    if (frenchOption != null) {
      interact(frenchOption::fire);  // Simulate selecting the menu item
    } else {
      fail("French option not found in language menu.");
    }

    String expected = simInfo.getString("splash_welcome");
    Parent newRoot = myController.getStage().getScene().getRoot();
    Text resultNode = (Text) newRoot.lookup(".text-welcome");
    String result = resultNode.getText();
    assertEquals(expected, result);
  }

  @Test
  void buttonClick_languageSelectGerman_languageChanged () {
    ResourceBundle simInfo = ResourceBundle.getBundle("SimInfo", Locale.GERMAN);
    MenuButton languageSelect = (MenuButton) root.lookup(".language-select"); //not a button though
    //GIVEN, splash screen is loaded
    //WHEN, germanLanguage is selected, language switches to german
    clickOn(languageSelect);
    MenuItem germanOption = null;
    for (MenuItem item : languageSelect.getItems()) {
      if (item.getText().equals("German")) {
        germanOption = item;
        break;
      }
    }
    if (germanOption != null) {
      interact(germanOption::fire);  // Simulate selecting the menu item
    } else {
      fail("German option not found in language menu.");
    }

    String expected = simInfo.getString("splash_welcome");
    Parent newRoot = myController.getStage().getScene().getRoot();
    Text resultNode = (Text) newRoot.lookup(".text-welcome");
    String result = resultNode.getText();
    assertEquals(expected, result);
  }

  @Test
  void buttonClick_languageSelectItalian_languageChanged () {
    ResourceBundle simInfo = ResourceBundle.getBundle("SimInfo", Locale.ITALIAN);
    MenuButton languageSelect = (MenuButton) root.lookup(".language-select"); //not a button though
    //GIVEN, splash screen is loaded
    //WHEN, italianLanguage is selected, language switches to italian
    clickOn(languageSelect);
    MenuItem italianOption = null;
    for (MenuItem item : languageSelect.getItems()) {
      if (item.getText().equals("Italian")) {
        italianOption = item;
        break;
      }
    }
    if (italianOption != null) {
      interact(italianOption::fire);  // Simulate selecting the menu item
    } else {
      fail("Italian option not found in language menu.");
    }

    String expected = simInfo.getString("splash_welcome");
    Parent newRoot = myController.getStage().getScene().getRoot();
    Text resultNode = (Text) newRoot.lookup(".text-welcome");
    String result = resultNode.getText();
    assertEquals(expected, result);
  }

  @Test
  //Negative test to ensure language doesn't change and no errors thrown when currently displayed language is selected again
  void buttonClick_languageSelectEnglish_languageNotChanged () {
    ResourceBundle simInfo = ResourceBundle.getBundle("SimInfo", Locale.ENGLISH);
    MenuButton languageSelect = (MenuButton) root.lookup(".language-select"); //not a button though
    //GIVEN, splash screen is loaded
    //WHEN, englishLanguage is selected, language does not change (default state is english)
    clickOn(languageSelect);
    MenuItem englishOption = null;
    for (MenuItem item : languageSelect.getItems()) {
      if (item.getText().equals("English")) {
        englishOption = item;
        break;
      }
    }
    if (englishOption != null) {
      interact(englishOption::fire);  // Simulate selecting the menu item
    } else {
      fail("Italian option not found in language menu.");
    }

    String expected = simInfo.getString("splash_welcome");
    Parent newRoot = myController.getStage().getScene().getRoot();
    Text resultNode = (Text) newRoot.lookup(".text-welcome");
    String result = resultNode.getText();
    assertEquals(expected, result);
  }

  @Test
  void buttonClick_themeSelectDark_themeChanges () {
    MenuButton themeSelect = (MenuButton) root.lookup(".color-scheme");
    //GIVEN, splash screen is loaded
    //WHEN, theme is changed to DARK, color scheme is changed
    clickOn(themeSelect);
    MenuItem darkOption = null;
    for (MenuItem item : themeSelect.getItems()) {
      if (item.getText().equals("Dark Mode")) {
        darkOption = item;
        break;
      }
    }
    if (darkOption != null) {
      interact(darkOption::fire);  // Simulate selecting the menu item
    } else {
      fail("Dark color theme option not found in theme select menu.");
    }
    Region background = (Region) myController.getStage().getScene().getRoot();
    Color backgroundColor = null;
    if (background.getBackground() != null && !background.getBackground().getFills().isEmpty()) {
      backgroundColor = (Color) background.getBackground().getFills().getFirst().getFill();
    }
    assertEquals(Color.web("#333333"), backgroundColor);
  }

  @Test
  void buttonClick_themeSelectDuke_themeChanges () {
    MenuButton themeSelect = (MenuButton) root.lookup(".color-scheme");
    //GIVEN, splash screen is loaded
    //WHEN, theme is changed to DUKE, color scheme is changed
    clickOn(themeSelect);
    MenuItem dukeOption = null;
    for (MenuItem item : themeSelect.getItems()) {
      if (item.getText().equals("Duke Mode")) {
        dukeOption = item;
        break;
      }
    }
    if (dukeOption != null) {
      interact(dukeOption::fire);  // Simulate selecting the menu item
    } else {
      fail("Duke color theme option not found in theme select menu.");
    }
    Region background = (Region) myController.getStage().getScene().getRoot();
    Color backgroundColor = null;
    if (background.getBackground() != null && !background.getBackground().getFills().isEmpty()) {
      backgroundColor = (Color) background.getBackground().getFills().getFirst().getFill();
    }
    assertEquals(Color.web("#003080"), backgroundColor);
  }

  @Test
  void buttonClick_themeSelectUNC_themeChanges () {
    MenuButton themeSelect = (MenuButton) root.lookup(".color-scheme");
    //GIVEN, splash screen is loaded
    //WHEN, theme is changed to UNC, color scheme is changed
    clickOn(themeSelect);
    MenuItem uncOption = null;
    for (MenuItem item : themeSelect.getItems()) {
      if (item.getText().equals("UNC Mode")) {
        uncOption = item;
        break;
      }
    }
    if (uncOption != null) {
      interact(uncOption::fire);  // Simulate selecting the menu item
    } else {
      fail("UNC color theme option not found in theme select menu.");
    }
    Region background = (Region) myController.getStage().getScene().getRoot();
    Color backgroundColor = null;
    if (background.getBackground() != null && !background.getBackground().getFills().isEmpty()) {
      backgroundColor = (Color) background.getBackground().getFills().getFirst().getFill();
    }
    assertEquals(Color.web("#89CFF0"), backgroundColor);
  }

  @Test
  //negative test, default theme is LIGHT so expect no change to UI
  void buttonClick_themeSelectLight_themeNoChange () {
    MenuButton themeSelect = (MenuButton) root.lookup(".color-scheme");
    //GIVEN, splash screen is loaded
    //WHEN, theme is changed to LIGHT, color scheme does not change
    clickOn(themeSelect);
    MenuItem lightOption = null;
    for (MenuItem item : themeSelect.getItems()) {
      if (item.getText().equals("Light Mode")) {
        lightOption = item;
        break;
      }
    }
    if (lightOption != null) {
      interact(lightOption::fire);  // Simulate selecting the menu item
    } else {
      fail("Light color theme option not found in theme select menu.");
    }
    Region background = (Region) myController.getStage().getScene().getRoot();
    Color backgroundColor = null;
    if (background.getBackground() != null && !background.getBackground().getFills().isEmpty()) {
      backgroundColor = (Color) background.getBackground().getFills().getFirst().getFill();
    }
    assertEquals(Color.web("#FAF9F6"), backgroundColor);
  }


}
