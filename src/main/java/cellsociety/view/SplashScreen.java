package cellsociety.view;


import cellsociety.view.GridView.ColorScheme;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

/**
 * @Author Palo Silva Class that handles the front end logic for displaying splash screen
 */

public class SplashScreen {

  private BorderPane splashPane;
  private Scene splashScene;
  private HBox controls;
  private SimulationController myController;
  private int width = 600;
  private int height = 800;


  public SplashScreen(SimulationController controller) {
    myController = controller;
    splashPane = new BorderPane();
    splashPane.getStyleClass().add("splash-pane");
    splashScene = new Scene(splashPane, width, height);

  }

  /**
   * Loads splash screen to stage using controller
   */
  public void loadScreen() {
    ResourceBundle simInfo = ResourceBundle.getBundle("SimInfo", myController.getLocale());
    loadSplashText(simInfo);
    loadControlButtons(simInfo);
    setSplashTheme(myController.getScheme(), simInfo);
    myController.displaySplashScreen(this.splashScene);

  }


  /**
   * Generates text on opening screen
   *
   * @param simInfo : resource bundle containing hardcoded simulation text
   */
  private void loadSplashText(ResourceBundle simInfo) {
    Text welcome = new Text(simInfo.getString("splash_welcome"));
    //welcome.setId("welcome");
    welcome.getStyleClass().add("text-welcome");
    TextFlow textFlow = new TextFlow(welcome);
    textFlow.setTextAlignment(TextAlignment.CENTER);
    splashPane.setCenter(textFlow);
  }

  /**
   * Generates control customization buttons for opening screen
   *
   * @param simInfo: resource bundle containing hardcoded simulation text
   */
  private void loadControlButtons(ResourceBundle simInfo) {
    List<MenuButton> controlButtons = new ArrayList<>();
    MenuButton languageSelect = new MenuButton(simInfo.getString("splash_language_button"));
    languageSelect.getStyleClass().add("language-select");
    MenuItem language1 = new MenuItem(simInfo.getString("splash_language_1"));
    MenuItem language2 = new MenuItem(simInfo.getString("splash_language_2"));
    MenuItem language3 = new MenuItem(simInfo.getString("splash_language_3"));
    MenuItem language4 = new MenuItem(simInfo.getString("splash_language_4"));
    language1.setOnAction(e -> {
      if (myController.getLocale() != Locale.ENGLISH) {
        myController.setLocale(Locale.ENGLISH);
        loadScreen();
      }
    });
    language2.setOnAction(e -> {
      if (myController.getLocale() != Locale.FRENCH) {
        myController.setLocale(Locale.FRENCH);
        loadScreen();
      }
    });
    language3.setOnAction(e -> {
      if (myController.getLocale() != Locale.GERMAN) {
        myController.setLocale(Locale.GERMAN);
        loadScreen();
      }
    });
    language4.setOnAction(e -> {
      if (myController.getLocale() != Locale.ITALIAN) {
        myController.setLocale(Locale.ITALIAN);
        loadScreen();
      }
    });
    languageSelect.getItems().addAll(language1, language2, language3, language4);
    MenuButton colorScheme = new MenuButton(simInfo.getString("splash_color_button"));
    colorScheme.getStyleClass().add("color-scheme");
    MenuItem colorScheme1 = new MenuItem(simInfo.getString("splash_color_scheme_1"));
    MenuItem colorScheme2 = new MenuItem(simInfo.getString("splash_color_scheme_2"));
    MenuItem colorScheme3 = new MenuItem(simInfo.getString("splash_color_scheme_3"));
    MenuItem colorScheme4 = new MenuItem(simInfo.getString("splash_color_scheme_4"));
    colorScheme1.setOnAction(e -> {
      myController.setScheme(ColorScheme.DARK);
      loadScreen();
    });
    colorScheme2.setOnAction(e -> {
      myController.setScheme(ColorScheme.LIGHT);
      loadScreen();
    });
    colorScheme3.setOnAction(e -> {
      myController.setScheme(ColorScheme.DUKE);
      loadScreen();
    });
    colorScheme4.setOnAction(e -> {
      myController.setScheme(ColorScheme.UNC);
      loadScreen();
    });
    colorScheme.getItems().addAll(colorScheme1, colorScheme2, colorScheme3, colorScheme4);
    controlButtons.add(languageSelect);
    controlButtons.add(colorScheme);
    Button loadButton = new Button(simInfo.getString("splash_load_sim"));
    loadButton.setOnAction(e -> {
      File newFile = myController.getFileChooser().showOpenDialog(myController.getStage());
      if (newFile != null) {
        //myController.getSplashLoop().stop();  //NEW
        myController.loadSimulation(newFile);
      }
    });
    loadButton.getStyleClass().add("load-button");
    controls = new HBox(10, loadButton);
    controls.getStyleClass().add("controls");
    for (MenuButton controlButton : controlButtons) {
      controls.getChildren().add(controlButton);
    }
    splashPane.setBottom(controls);

  }

  /**
   * Sets theme of simulation
   *
   * @param scheme   : color scheme
   * @param simInfo: resource bundle containing hardcoded simulation text
   */
  private void setSplashTheme(ColorScheme scheme, ResourceBundle simInfo) {
    URL resourcePath = switch (scheme) {
      case DARK -> getClass().getResource("/SplashDark.css");
      case LIGHT -> getClass().getResource("/SplashLight.css");
      case DUKE -> getClass().getResource("/SplashDuke.css");
      case UNC -> getClass().getResource("/SplashUnc.css");
    };

    if (resourcePath == null) {
      System.err.println(simInfo.getString("invalid_theme"));
    }
    splashScene.getStylesheets().clear();
    splashScene.getStylesheets().add(resourcePath.toExternalForm());

  }

  public Scene getSplashScene() {
    return splashScene;
  }


}
