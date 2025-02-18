// Parser.java
package cellsociety.parser;

import java.util.Map;

public interface Parser {
    /**
     * @return width of the simulation display
     */
    int getWidth();

    /**
     * @return height of the simulation display
     */
    int getHeight();

    /**
     * @return title of the simulation
     */
    String getTitle();

    /**
     * @return number of rows in the grid
     */
    int getRows();

    /**
     * @return number of columns in the grid
     */
    int getColumns();

    /**
     * @return array of initial states for the simulation
     */
    String[] getInitialStates();

    /**
     * @return type of simulation being run
     */
    String getSimType();

    /**
     * @return map of simulation variables and their values
     */
    Map<String, String> getSimVarsMap();
}