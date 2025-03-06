package cellsociety.parser;

import java.util.Map;
import java.util.Set;

public abstract class Parser {

  private final static Map<String, Set<String>> simulationStatesMap = Map.of(
      "Conway", Set.of("A", "D"),
      "Fire", Set.of("B", "T", "E"),
      "Percolation", Set.of("BL", "P", "O"),
      "Segregation", Set.of("BLU", "R", "EM"),
      "WatorWorld", Set.of("S", "F", "W"),
      "GeneralConway", Set.of("D", "A"),
      "Sugarscape", Set.of("PATCH", "AGENT")
  );
  private final static Set<String> requiresValues = Set.of("Sugarscape");

  //what info is needed from Parser
  private String simulationType;
  private String author;
  private String title;
  private String description;
  private int width;
  private int height;
  private int rows;
  private int columns;

  //we calculate cellSize (based on width, height, rows, columns
  public Parser() {
  }

  public static boolean isInSimulation(String state, String simulation) {
    return simulationStatesMap.get(simulation).contains(state);
  }

  public static boolean validateSimulation(String state) {
    return simulationStatesMap.containsKey(state);
  }

  public static boolean requiresValues(String state) {
    return requiresValues.contains(state);
  }

  public abstract int getWidth();

  public abstract int getHeight();

  public abstract String getTitle();

  public abstract int getRows();

  public abstract int getColumns();

  public abstract String[] getInitialStates();

  public abstract String getSimType();

  public abstract Map<String, String> getSimVarsMap();


}
