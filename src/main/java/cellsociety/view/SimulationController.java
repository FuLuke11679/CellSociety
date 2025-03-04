package cellsociety.view;

import cellsociety.view.GridView.ColorScheme;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SimulationController {
  private final FileChooser FILE_CHOOSER = new FileChooser();
  private Timeline simLoop;
  private Timeline splashLoop;
  private static double SECOND_DELAY = 0.8;
  private Stage myStage;
  private File currentFile;
  private ColorScheme myScheme;
  private Locale myLocale;
  private SimulationScreen mySimScreen;

  public SimulationController(Stage stage, Locale locale){
    myStage = stage;
    myLocale = locale;
    myScheme = ColorScheme.LIGHT;
  }

  /**
   * Continuously updates splash screen as user makes customization choices
   */
  public void displaySplashScreen(Scene splashScene){
    splashLoop = new Timeline(new KeyFrame(Duration.seconds(0.05), e -> {
      setStage(splashScene);
    }));
    splashLoop.setCycleCount(Timeline.INDEFINITE);
    splashLoop.play();
  }


  /**
   * Function to load a new simulation
   * @param dataFile : XML File chosen by user from their local machine
   */
  public void loadSimulation(File dataFile) {
    if(splashLoop.getStatus() == Animation.Status.RUNNING){
      splashLoop.stop();
    }
    else if(simLoop.getStatus() == Animation.Status.RUNNING){
      simLoop.stop();
    }
    currentFile = dataFile;
    //System.out.println(currentFile);
    mySimScreen = new SimulationScreen(dataFile, this);

  }

  /**
   * Function that begins execution of the simulation loop
   */

  public void startSimulation() {
    if (simLoop == null) {
      simLoop = new Timeline(new KeyFrame(Duration.seconds(SECOND_DELAY), e -> {
        mySimScreen.update();
      }));
      simLoop.setCycleCount(Timeline.INDEFINITE);
      simLoop.setRate(1.0 / SECOND_DELAY);  // Ensure speed is set correctly
    }
    if (simLoop.getStatus() == Animation.Status.PAUSED) {
      simLoop.play();  // Resume if paused
    } else if (simLoop.getStatus() != Animation.Status.RUNNING) {
      simLoop.playFromStart();  // Start fresh if it wasn't running
    }
  }

  /**
   * Saves simulation to an xml file
   * @param simInfo resource bundle containing hardcoded simulation text
   */
  public void saveSimulation(ResourceBundle simInfo) {
    TextInputDialog dialog = new TextInputDialog(simInfo.getString("sim"));
    dialog.setHeaderText(simInfo.getString("prompt"));
    dialog.setContentText(simInfo.getString("metadata"));
    dialog.showAndWait();

    File saveFile = FILE_CHOOSER.showSaveDialog(myStage);
    if (saveFile != null) {
      try {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = doc.createElement("Simulation");
        root.setAttribute("Title", dialog.getEditor().getText());
        doc.appendChild(root);

        Element grid = doc.createElement("Grid");
        grid.setAttribute("rows", String.valueOf(mySimScreen.getMyParser().getRows()));
        grid.setAttribute("columns", String.valueOf(mySimScreen.getMyParser().getColumns()));
        root.appendChild(grid);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(saveFile);
        transformer.transform(source, result);
      } catch (Exception e) {
        showMessage(simInfo.getString("save_error"));
      }
    }
  }

  public void resetSimulation() {
    loadSimulation(currentFile);
  }

  public FileChooser getFileChooser(){
    return FILE_CHOOSER;
  }

  public Locale getLocale(){
    return myLocale;
  }

  public void setLocale(Locale locale){
    myLocale = locale;
  }

  public ColorScheme getScheme(){
    return myScheme;
  }

  public void setScheme(ColorScheme scheme){
    myScheme = scheme;
  }

  public Stage getStage(){
    return myStage;
  }

  public void setStage(Scene scene) {
    myStage.setScene(scene);
    myStage.show();
  }

  public Timeline getSimLoop(){
    return simLoop;
  }

  public void setSimLoop(Timeline simLoop){
    this.simLoop = simLoop;
  }


  public void showMessage(String message) {
    new Alert(Alert.AlertType.INFORMATION, message).showAndWait();
  }

}
