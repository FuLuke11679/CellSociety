package cellsociety.view;


import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
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


public class SplashScreenTest extends DukeApplicationTest {

  private SimulationController myController;
  private SplashScreen mySplashScreen;
  private Parent root;


  @Override
  public void start (Stage stage) {
    // create application and add scene for testing to given stage
    myController = new SimulationController(stage, Locale.ENGLISH);
    mySplashScreen = new SplashScreen(myController);
    mySplashScreen.loadScreen();
    //delay 1 second after loading screen to allow for all UI updates to complete
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      myController.showMessage(e.getMessage());
    }
    root = mySplashScreen.getSplashScene().getRoot();

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
    //GIVEN, splash screen is loaded
    //WHEN, frenchLanguage is selected, language switches to french
    ResourceBundle simInfo = ResourceBundle.getBundle("SimInfo", Locale.FRENCH);
    Platform.runLater(()-> {
          MenuButton languageSelect = (MenuButton) root.lookup(".language-select");
          interact(languageSelect::fire);
          MenuItem frenchOption = null;
          for (MenuItem item : languageSelect.getItems()) {
            if (item.getText().equals("French")) {
              frenchOption = item;
              break;
            }
          }
          if (frenchOption != null) {
            interact(frenchOption::fire);
          } else {
            fail("French option not found in language menu.");
          }
          String expected = simInfo.getString("splash_welcome");
          Text resultNode = (Text) root.lookup(".text-welcome");
          String result = resultNode.getText();
          myController.getSplashLoop().stop();
          assertEquals(expected, result);
    });

  }


  @Test
  void buttonClick_languageSelectGerman_languageChanged () {
    //GIVEN, splash screen is loaded
    //WHEN, germanLanguage is selected, language switches to german
    ResourceBundle simInfo = ResourceBundle.getBundle("SimInfo", Locale.GERMAN);
    Platform.runLater(()-> {
          MenuButton languageSelect = (MenuButton) root.lookup(".language-select");
          myController.setStage(root.getScene());
          interact(languageSelect::fire);
          MenuItem germanOption = null;
          for (MenuItem item : languageSelect.getItems()) {
            if (item.getText().equals("German")) {
              germanOption = item;
              break;
            }
          }
          if (germanOption != null) {
            interact(germanOption::fire);
          } else {
            fail("German option not found in language menu.");
          }
          String expected = simInfo.getString("splash_welcome");
          Text resultNode = (Text) root.lookup(".text-welcome");
          String result = resultNode.getText();
          myController.getSplashLoop().stop();
          assertEquals(expected, result);
    });

  }

  @Test
  void buttonClick_languageSelectItalian_languageChanged () {
    //GIVEN, splash screen is loaded
    //WHEN, italianLanguage is selected, language switches to italian
    ResourceBundle simInfo = ResourceBundle.getBundle("SimInfo", Locale.ITALIAN);
    Platform.runLater(()-> {
      MenuButton languageSelect = (MenuButton) root.lookup(".language-select");
      myController.setStage(root.getScene());
      interact(languageSelect::fire);
      MenuItem italianOption = null;
      for (MenuItem item : languageSelect.getItems()) {
        if (item.getText().equals("Italian")) {
          italianOption = item;
          break;
        }
      }
      if (italianOption != null) {
        interact(italianOption::fire);
      } else {
        fail("Italian option not found in language menu.");
      }
      String expected = simInfo.getString("splash_welcome");
      Text resultNode = (Text) root.lookup(".text-welcome");
      String result = resultNode.getText();
      myController.getSplashLoop().stop();
      assertEquals(expected, result);
    });

  }


  @Test
    //NEGATIVE TEST, EXPECT NO CHANGE TO UI
  void buttonClick_languageSelectEnglish_languageNotChanged () {
    //GIVEN, splash screen is loaded
    //WHEN, englishLanguage is selected, language does change (english is default language)
    ResourceBundle simInfo = ResourceBundle.getBundle("SimInfo", Locale.ENGLISH);
    Platform.runLater(()-> {
      MenuButton languageSelect = (MenuButton) root.lookup(".language-select");

      myController.setStage(root.getScene());
      interact(languageSelect::fire);
      MenuItem englishOption = null;
      for (MenuItem item : languageSelect.getItems()) {
        if (item.getText().equals("English")) {
          englishOption = item;
          break;
        }
      }
      if (englishOption != null) {
        interact(englishOption::fire);
      } else {
        fail("English option not found in language menu.");
      }
      String expected = simInfo.getString("splash_welcome");
      Text resultNode = (Text) root.lookup(".text-welcome");
      String result = resultNode.getText();
      myController.getSplashLoop().stop();
      assertEquals(expected, result);
    });

  }

  @Test
  void buttonClick_themeSelectDark_themeChanges () {
    //GIVEN, splash screen is loaded
    //WHEN, darkTheme is selected, theme switches to dark
    Platform.runLater(()-> {
      MenuButton themeSelect = (MenuButton) root.lookup(".color-scheme");
      myController.setStage(root.getScene());
      interact(themeSelect::fire);
      MenuItem darkOption = null;
      for (MenuItem item : themeSelect.getItems()) {
        if (item.getText().equals("Dark Mode")) {
          darkOption = item;
          break;
        }
      }
      if (darkOption != null) {
        interact(darkOption::fire);
      } else {
        fail("Dark color theme option not found in theme select menu.");
      }
      Platform.runLater(()-> {
        Region background = (Region) myController.getStage().getScene().getRoot();
        Color backgroundColor = null;
        if (background.getBackground() != null && !background.getBackground().getFills().isEmpty()) {
          backgroundColor = (Color) background.getBackground().getFills().getFirst().getFill();
        }
        myController.getSplashLoop().stop();
        assertEquals(Color.web("#333333"), backgroundColor);
      });

    });

  }

  @Test
  void buttonClick_themeSelectDuke_themeChanges () {
    //GIVEN, splash screen is loaded
    //WHEN, dukeTheme is selected, theme switches to duke
    Platform.runLater(()-> {
      MenuButton themeSelect = (MenuButton) root.lookup(".color-scheme");
      myController.setStage(root.getScene());
      interact(themeSelect::fire);
      MenuItem dukeOption = null;
      for (MenuItem item : themeSelect.getItems()) {
        if (item.getText().equals("Duke Mode")) {
          dukeOption = item;
          break;
        }
      }
      if (dukeOption != null) {
        interact(dukeOption::fire);
      } else {
        fail("Duke color theme option not found in theme select menu.");
      }
      Platform.runLater(()-> {
        Region background = (Region) myController.getStage().getScene().getRoot();
        Color backgroundColor = null;
        if (background.getBackground() != null && !background.getBackground().getFills().isEmpty()) {
          backgroundColor = (Color) background.getBackground().getFills().getFirst().getFill();
        }
        myController.getSplashLoop().stop();
        assertEquals(Color.web("#003080"), backgroundColor);
      });
    });
  }

  @Test
  void buttonClick_themeSelectUNC_themeChanges () {
    //GIVEN, splash screen is loaded
    //WHEN, uncTheme is selected, theme switches to unc
    Platform.runLater(()-> {
      MenuButton themeSelect = (MenuButton) root.lookup(".color-scheme");
      myController.setStage(root.getScene());
      interact(themeSelect::fire);
      MenuItem uncOption = null;
      for (MenuItem item : themeSelect.getItems()) {
        if (item.getText().equals("UNC Mode")) {
          uncOption = item;
          break;
        }
      }
      if (uncOption != null) {
        interact(uncOption::fire);
      } else {
        fail("UNC color theme option not found in theme select menu.");
      }
      Platform.runLater(()-> {
        Region background = (Region) myController.getStage().getScene().getRoot();
        Color backgroundColor = null;
        if (background.getBackground() != null && !background.getBackground().getFills().isEmpty()) {
          backgroundColor = (Color) background.getBackground().getFills().getFirst().getFill();
        }
        myController.getSplashLoop().stop();
        assertEquals(Color.web("#89CFF0"), backgroundColor);
      });

    });

  }

  @Test
  //NEGATIVE TEST, EXPECT NO CHANGE TO UI
  void buttonClick_themeSelectLight_themeNoChange () {
    //GIVEN, splash screen is loaded
    //WHEN, lightTheme is selected, theme does not change (light theme is default)
    Platform.runLater(()-> {
      MenuButton themeSelect = (MenuButton) root.lookup(".color-scheme");
      myController.setStage(root.getScene());
      interact(themeSelect::fire);
      MenuItem lightOption = null;
      for (MenuItem item : themeSelect.getItems()) {
        if (item.getText().equals("Light Mode")) {
          lightOption = item;
          break;
        }
      }
      if (lightOption != null) {
        interact(lightOption::fire);
      } else {
        fail("Light color theme option not found in theme select menu.");
      }
      Platform.runLater(()-> {
        Region background = (Region) myController.getStage().getScene().getRoot();
        Color backgroundColor = null;
        if (background.getBackground() != null && !background.getBackground().getFills().isEmpty()) {
          backgroundColor = (Color) background.getBackground().getFills().getFirst().getFill();
        }
        myController.getSplashLoop().stop();
        assertEquals(Color.web("#FAF9F6"), backgroundColor);
      });
    });
  }


}
