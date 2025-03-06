package cellsociety.model.grid;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.ConwayCell.ConwayState;
import cellsociety.model.cell.FireCell.FireState;
import cellsociety.model.cell.PercolationCell.PercolationState;
import cellsociety.model.cell.SegregationCell.SegregationState;
import cellsociety.model.cell.WatorCell.WatorState;
import cellsociety.model.factory.CellFactory;
import cellsociety.model.grid.handler.EdgeHandler;
import cellsociety.model.grid.neighborhood.NeighborhoodStrategy;
import cellsociety.model.grid.shape.CellShape;
import cellsociety.model.ruleset.Ruleset;
import cellsociety.model.ruleset.SugarscapeRuleset;
import cellsociety.model.state.CellState;
import cellsociety.model.state.SugarscapeState;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Updates Grid based on Cell logic. This abstract class manages a grid of cells and applies a given
 * ruleset to update cell states. It does not handle any UI or JavaFX display logic.
 * <p>
 * The grid is initialized using a provided array of state symbols, which are mapped to specific
 * {@link CellState} instances via a static state map.
 *
 * @author Luke
 * @author Palo
 * @author Daniel
 */
public abstract class Grid {

  /**
   * Mapping from state symbols to their corresponding CellState objects.
   */
  protected final static Map<String, CellState> STATE_MAP = Map.ofEntries(
      Map.entry("A", ConwayState.ALIVE),
      Map.entry("D", ConwayState.DEAD),
      Map.entry("B", FireState.BURNING),
      Map.entry("T", FireState.TREE),
      Map.entry("E", FireState.EMPTY),
      Map.entry("BL", PercolationState.BLOCKED),
      Map.entry("P", PercolationState.PERCOLATED),
      Map.entry("O", PercolationState.OPEN),
      Map.entry("BLU", SegregationState.BLUE),
      Map.entry("R", SegregationState.RED),
      Map.entry("EM", SegregationState.EMPTY),
      Map.entry("S", WatorState.SHARK),
      Map.entry("F", WatorState.FISH),
      Map.entry("W", WatorState.WATER),
      Map.entry("PATCH", SugarscapeState.PATCH),
      Map.entry("AGENT", SugarscapeState.AGENT)
  );

  private int rows;
  private int columns;
  private final String[] myCells;
  private List<List<Cell>> myGrid;
  private final Ruleset ruleset;
  private EdgeHandler edgeHandler;
  private NeighborhoodStrategy neighborhoodStrategy;
  private CellShape cellShape;

  /**
   * Constructs a Grid with the specified dimensions, ruleset, and initial cell states.
   *
   * @param rows    the number of rows in the grid
   * @param columns the number of columns in the grid
   * @param ruleset the {@link Ruleset} to apply to this grid
   * @param cells   an array of state symbols representing the initial states of the cells
   */
  public Grid(int rows, int columns, Ruleset ruleset, String[] cells) {
    this.rows = rows;
    this.columns = columns;
    this.ruleset = ruleset;
    this.myCells = cells;
    initializeGrid();
  }

  /**
   * Sets the edge handling strategy for the grid.
   *
   * @param handler the {@link EdgeHandler} to be used for processing edge cases
   */
  public void setEdgeHandler(EdgeHandler handler) {
    this.edgeHandler = handler;
  }

  /**
   * Sets the neighborhood strategy for the grid.
   *
   * @param strategy the {@link NeighborhoodStrategy} to be used to select cell neighbors
   */
  public void setNeighborhoodStrategy(NeighborhoodStrategy strategy) {
    this.neighborhoodStrategy = strategy;
  }

  /**
   * Sets the cell shape strategy for the grid.
   *
   * @param shape the {@link CellShape} that defines how cell positions and neighbor offsets are
   *              computed
   */
  public void setCellShape(CellShape shape) {
    this.cellShape = shape;
  }

  /**
   * Initializes the grid by creating cells based on the provided state symbols.
   * <p>
   * Each cell is created by mapping its state symbol to a {@link CellState} via
   * {@link #getInitialState(String)} and then dynamically instantiating the appropriate cell type.
   * </p>
   */
  public void initializeGrid() {
    myGrid = new ArrayList<>();
    int count = 0;

    for (int x = 0; x < rows; x++) {
      List<Cell> row = new ArrayList<>();
      for (int y = 0; y < columns; y++) {
        // Get the initial state of the cell
        CellState initialState = getInitialState(myCells[count]);

        // Determine initial sugar amount if the simulation is Sugarscape
        Integer initialSugar = (ruleset instanceof SugarscapeRuleset) ?
            ((SugarscapeRuleset) ruleset).getInitialValues()[count] : null;

        // Create the cell with or without initial sugar
        Cell cell = CellFactory.createCell(count, initialState, initialSugar);

        row.add(cell);
        count++;
      }
      myGrid.add(row);
    }
  }


  /**
   * Updates the grid for a single simulation step.
   * <p>
   * For each cell, the method obtains its neighbors and updates its state based on the rules
   * defined in the {@link Ruleset}. After processing all cells, the new states are applied.
   * </p>
   */
  public void update() {
    //return a list of cell ids that were changed,
    //loop over all cells and randomly change color of alive cells with probability 0.4
    int length = getLength();
    for (int id = 0; id < length; id++) {
      int row = id / columns;
      int col = id % columns;
      Cell cell = myGrid.get(row).get(col);
      // Retrieve neighbors for the current cell.
      List<Cell> neighbors = getNeighbors(row, col);
      // Update cell state based on the ruleset.
      ruleset.updateCellState(cell, new ArrayList<>(neighbors));
    }
    // Apply all updated states.
    moveNextStateToCurrent();
  }

  /**
   * Applies the next state for all cells by moving the generated next state into the current
   * state.
   * <p>
   * After updating all cells, this method ensures that each cell's state is properly updated for
   * the next simulation step.
   * </p>
   */
  protected void moveNextStateToCurrent() {
    for (int id = 0; id < getLength(); id++) {
      int row = id / columns;
      int col = id % columns;
      Cell cell = myGrid.get(row).get(col);
      cell.setCurrState(cell.getNextState());
      cell.setNextState(null);
    }
  }

  /**
   * Retrieves the cell at the specified grid position.
   *
   * @param row the row index of the desired cell
   * @param col the column index of the desired cell
   * @return the {@link Cell} located at (row, col) in the grid
   */
  public Cell getCell(int row, int col) {
    return myGrid.get(row).get(col);
  }


  /**
   * Retrieves the neighboring cells for the cell at the specified position.
   * <p>
   * The method uses the current {@link CellShape} to obtain a list of base neighbor offsets,
   * applies the {@link NeighborhoodStrategy} to select a subset of these offsets, and then
   * processes them using the {@link EdgeHandler} to account for grid boundaries.
   * </p>
   *
   * @param row the row index of the target cell
   * @param col the column index of the target cell
   * @return a list of neighboring {@link Cell} objects surrounding the cell at (row, col)
   */
  public List<Cell> getNeighbors(int row, int col) {
    // 1️Use cell shape to get base relative offsets
    List<int[]> finalOffsets = neighborhoodStrategy.getFinalOffsets(cellShape, row, col);
    return edgeHandler.handleNeighbors(row, col, finalOffsets, this);
  }

  /**
   * Computes the total number of cells in the grid.
   *
   * @return the total count of cells in the grid
   */
  public int getLength() {
    int totalCount = 0;
    for (List<Cell> list : myGrid) {
      totalCount += list.size();
    }
    return totalCount;
  }

  /**
   * Maps a state symbol to its corresponding {@link CellState} using the defined state map.
   *
   * @param stateSymbol the symbol representing the cell state
   * @return the {@link CellState} corresponding to the provided symbol
   */
  protected CellState getInitialState(String stateSymbol) {
    return STATE_MAP.get(stateSymbol);
  }

  public int getRows() {
    return rows;
  }

  public int getColumns() {
    return columns;
  }

  protected Ruleset getRuleset() {
    return ruleset;
  }

  public boolean isValidPosition(int row, int col) {
    return row >= 0 && row < rows && col >= 0 && col < columns;
  }

  /**
   * Expands the grid to include a specified new row and/or column.
   * <p>
   * If the provided newRow or newCol is negative, the grid is shifted to maintain valid indices.
   * New cells are created using the default state from the ruleset.
   * </p>
   *
   * @param newRow the new row index to include (can be negative)
   * @param newCol the new column index to include (can be negative)
   */
  public void expandGrid(int newRow, int newCol) {
    int rowShift = 0;
    int colShift = 0;

    // Determine if existing cells need to be shifted.
    if (newRow < 0) {
      rowShift = Math.abs(newRow);
    }
    if (newCol < 0) {
      colShift = Math.abs(newCol);
    }

    // Calculate new grid dimensions.
    int newRows = Math.max(rows + rowShift, newRow + 1);
    int newCols = Math.max(columns + colShift, newCol + 1);

    // Create a new grid with updated dimensions.
    List<List<Cell>> newGrid = new ArrayList<>();

    for (int i = 0; i < newRows; i++) {
      List<Cell> row = new ArrayList<>();
      for (int j = 0; j < newCols; j++) {
        if (i >= rowShift && j >= colShift && (i - rowShift) < rows && (j - colShift) < columns) {
          // Shift existing cell to new position.
          row.add(myGrid.get(i - rowShift).get(j - colShift));
        } else {
          // Create a new cell with the default state.
          CellState initialState = ruleset.getDefaultCellState();
        }
      }
      newGrid.add(row);
    }

    // Update grid properties.
    myGrid = newGrid;
    rows = newRows;
    columns = newCols;
  }

  /**
   * Updates a strategy (edge, neighborhood, or shape) used by the grid.
   * <p>
   * This method uses reflection to instantiate a new strategy object based on its fully qualified
   * class name, then assigns it to the appropriate handler.
   * </p>
   *
   * @param strategyType the type of strategy to update ("edge", "neighborhood", or "shape")
   * @param className    the fully qualified class name of the new strategy implementation
   */
  public void updateStrategy(String strategyType, String className) {
    try {
      Class<?> strategyClass = Class.forName(className);
      Object strategyInstance = strategyClass.getDeclaredConstructor().newInstance();

      if (strategyType.equalsIgnoreCase("edge")) {
        this.edgeHandler = (EdgeHandler) strategyInstance;
      } else if (strategyType.equalsIgnoreCase("neighborhood")) {
        this.neighborhoodStrategy = (NeighborhoodStrategy) strategyInstance;
      } else if (strategyType.equalsIgnoreCase("shape")) {
        this.cellShape = (CellShape) strategyInstance;
      } else {
        throw new IllegalArgumentException("Invalid strategy type: " + strategyType);
      }
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
             NoSuchMethodException |
             InvocationTargetException e) {
      throw new IllegalArgumentException(
          "No such strategy, neighborhood, or shape combination" + strategyType);
    }
  }

  /**
   * Switches the edge handling strategy to a new implementation.
   *
   * @param edgeClassName the fully qualified class name of the new {@link EdgeHandler}
   *                      implementation
   */
  public void switchEdgeHandler(String edgeClassName) {
    updateStrategy("edge", edgeClassName);
  }

  /**
   * Switches the neighborhood strategy to a new implementation.
   *
   * @param neighborhoodClassName the fully qualified class name of the new
   *                              {@link NeighborhoodStrategy} implementation
   */
  public void switchNeighborhood(String neighborhoodClassName) {
    updateStrategy("neighborhood", neighborhoodClassName);
  }

  /**
   * Switches the cell shape strategy to a new implementation.
   *
   * @param shapeClassName the fully qualified class name of the new {@link CellShape}
   *                       implementation
   */
  public void switchCellShape(String shapeClassName) {
    updateStrategy("shape", shapeClassName);
  }

  /**
   * Prints the current grid to the console.
   * <p>
   * Each cell's current state is printed in row-major order with each row on a new line.
   * </p>
   */
  public void printGrid() {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        System.out.print(getCell(i, j).getCurrState() + " ");
      }
      System.out.println();
    }
    System.out.println();
  }
}
