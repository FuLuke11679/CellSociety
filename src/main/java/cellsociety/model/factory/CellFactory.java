package cellsociety.model.factory;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.SugarscapePatch;
import cellsociety.model.state.CellState;
import cellsociety.parser.PropertiesLoader;
import java.lang.reflect.Constructor;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Author: Daniel Rodriguez-Florido
 *
 * Factory to generate any kind of cell based on the given state and id
 */
public class CellFactory {

  private static final Logger log = LogManager.getLogger(CellFactory.class);

  private static final String CELL_CLASS_PROPERTY_FILE_NAME = "cellsociety/cells.properties";
  private static final String CELL_STATE_PROPERTY_FILE_NAME = "cellsociety/cellstates.properties";

  /**
   * Creates a cell of the desired type based on the given unique CellState
   * @param id The id of the cell to create
   * @param state CellState indicating the state of the cell
   * @return New concrete Cell object which extends Cell
   */
  public static Cell createCell(int id, CellState state) {
    Properties cellClassProperties = new Properties();
    PropertiesLoader.loadPropertiesFolder(CELL_CLASS_PROPERTY_FILE_NAME, cellClassProperties);

    String cellType = getCellType(state);

    String cellClass = "";
    try {
      cellClass = cellClassProperties.getProperty(cellType);
    } catch (NullPointerException e) {
      log.error("No cell class contains this state {}", cellType);
      throw new IllegalArgumentException("No cell class contains this state " + cellType);
    }

    try {
      Class<? extends Cell> clazz = (Class<? extends Cell>) Class.forName(cellClass);

      // Handle SugarscapePatch separately
      if (clazz.equals(SugarscapePatch.class)) {
        Constructor<? extends Cell> constructor = clazz.getConstructor(int.class, CellState.class, CellState.class, int.class, int.class);

        // Default values for now (modify to pull from simulation parameters if available)
        int initialSugar = 5;
        int maxSugar = 25;

        return constructor.newInstance(id, state, null, initialSugar, maxSugar);
      } else {
        Constructor<? extends Cell> constructor = clazz.getConstructor(int.class, CellState.class, CellState.class);
        return constructor.newInstance(id, state, null);
      }

    } catch (Exception e) {
      log.error("Could not find or instantiate cell type: {}", cellType, e);
    }

    log.error("Could not find cell class: {}. Returned null object.", cellClass);
    return null;
  }


  /**
   * Function to figure out what type of cell to make depending on the unique state
   * @param state The state I am looking for
   * @return The Cell type the state belongs to
   */
  private static String getCellType(CellState state) {
    Properties cellStateProperties = new Properties();
    PropertiesLoader.loadPropertiesFolder(CELL_STATE_PROPERTY_FILE_NAME, cellStateProperties);

    for (String key : cellStateProperties.stringPropertyNames()) {
      String[] states = cellStateProperties.getProperty(key).split(",");
      for (String myState : states) {
        if (myState.equals(state.toString())) {
          return key;
        }
      }
    }

    return null;
  }

}
