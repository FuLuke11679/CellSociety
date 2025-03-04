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

public class SplashScreen {

  private BorderPane splashPane;
  private Scene splashScene;
  //private Locale myLocale;
  private ColorScheme myScheme; //initialize to LIGHT mode
  private HBox controls;
  private SimulationController myController;


  public SplashScreen(SimulationController controller){
    //what variables do we need to set?
    //myLocale = locale;
    myController = controller;
    splashPane = new BorderPane();
    splashScene = new Scene(splashPane, 600, 800);
    //myScheme = ColorScheme.LIGHT;  //default to light theme
    //loadScreen();
  }

  public void loadScreen(){
    ResourceBundle simInfo = ResourceBundle.getBundle("SimInfo", myController.getLocale());
    loadSplashText(simInfo);
    loadControlButtons(simInfo);
    setSplashTheme(myController.getScheme(), simInfo);
    myController.displaySplashScreen(this.splashScene);
  }


  /**
   * Generates text on opening screen
   * @param simInfo : resource bundle containing hardcoded simulation text
   */
  private void loadSplashText(ResourceBundle simInfo) {
    Text welcome = new Text(simInfo.getString("splash_welcome"));
    TextFlow textFlow = new TextFlow(welcome);
    textFlow.setTextAlignment(TextAlignment.CENTER);
    splashPane.setCenter(textFlow);
  }

  /**
   * Generates control customization buttons for opening screen
   * @param simInfo: resource bundle containing hardcoded simulation text
   */
  private void loadControlButtons(ResourceBundle simInfo) {
    List<MenuButton> controlButtons = new ArrayList<>();
    MenuButton languageSelect = new MenuButton(simInfo.getString("splash_language_button"));
    MenuItem language1 = new MenuItem(simInfo.getString("splash_language_1"));
    MenuItem language2 = new MenuItem(simInfo.getString("splash_language_2"));
    MenuItem language3 = new MenuItem(simInfo.getString("splash_language_3"));
    MenuItem language4 = new MenuItem(simInfo.getString("splash_language_4"));
    language1.setOnAction(e -> {
      if(myController.getLocale() != Locale.ENGLISH) {
        //myLocale = Locale.ENGLISH;
        myController.setLocale(Locale.ENGLISH);
        loadScreen();
      }
    });
    language2.setOnAction(e -> {
      if(myController.getLocale() != Locale.FRENCH) {
        //myLocale = Locale.FRENCH;
        myController.setLocale(Locale.FRENCH);
        loadScreen();
      }
    });
    language3.setOnAction(e ->{
      if(myController.getLocale() != Locale.GERMAN){
        myController.setLocale(Locale.GERMAN);
        loadScreen();
      }
    });
    language4.setOnAction(e ->{
      if(myController.getLocale() != Locale.ITALIAN){
        myController.setLocale(Locale.ITALIAN);
        loadScreen();
      }
    });
    languageSelect.getItems().addAll(language1, language2, language3, language4);
    MenuButton colorScheme = new MenuButton(simInfo.getString("splash_color_button"));
    MenuItem colorScheme1 = new MenuItem(simInfo.getString("splash_color_scheme_1"));
    MenuItem colorScheme2 = new MenuItem(simInfo.getString("splash_color_scheme_2"));
    MenuItem colorScheme3 = new MenuItem(simInfo.getString("splash_color_scheme_3"));
    MenuItem colorScheme4 = new MenuItem(simInfo.getString("splash_color_scheme_4"));
    colorScheme1.setOnAction(e -> {
      myController.setScheme(ColorScheme.DARK); //this should be eliminated
      loadScreen();
    });
    colorScheme2.setOnAction(e -> {
      //myScheme = ColorScheme.LIGHT;
      myController.setScheme(ColorScheme.LIGHT);
      loadScreen();
    });
    colorScheme3.setOnAction(e -> {
      //myScheme = ColorScheme.DUKE;
      myController.setScheme(ColorScheme.DUKE);
      loadScreen();
    });
    colorScheme4.setOnAction(e -> {
      //myScheme = ColorScheme.UNC;
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
        myController.loadSimulation(newFile);
      }
    });
    controls = new HBox(10, loadButton);
    for (MenuButton controlButton : controlButtons) {
      controls.getChildren().add(controlButton);
    }
    splashPane.setBottom(controls);

  }

  /**
   * Sets theme of simulation
   * @param scheme : color scheme
   * @param simInfo: resource bundle containing hardcoded simulation text
   */
  private void setSplashTheme(ColorScheme scheme, ResourceBundle simInfo){
    URL resourcePath = null;
    switch(scheme){
      case DARK:
        resourcePath = getClass().getResource("/SplashDark.css");
        break;
      case LIGHT:
        resourcePath = getClass().getResource("/SplashLight.css");
        break;
      case DUKE:
        resourcePath = getClass().getResource("/SplashDuke.css");
        break;
      case UNC:
        resourcePath = getClass().getResource("/SplashUnc.css");
        break;
    }

    if (resourcePath == null) {
      System.err.println(simInfo.getString("invalid_theme"));
    }
    splashScene.getStylesheets().add(resourcePath.toExternalForm());

  }

  public Scene getSplashScene() {
    return splashScene;
  }

  /*
  public Locale getMyLocale(){
    return myLocale;
  }

   */
  public HBox getControls(){
    return controls;
  }
  public ColorScheme getColorScheme(){
    return myScheme;
  }

  public BorderPane getSplashPane(){
    return splashPane;
  }

}
