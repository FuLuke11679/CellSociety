package cellsociety.model.grid;

import cellsociety.model.cell.Cell;
import cellsociety.model.factory.CellFactory;
import cellsociety.model.cell.ConwayCell.ConwayState;
import cellsociety.model.cell.FireCell.FireState;
import cellsociety.model.cell.PercolationCell.PercolationState;
import cellsociety.model.cell.SegregationCell.SegregationState;
import cellsociety.model.grid.handler.EdgeHandler;
import cellsociety.model.grid.neighborhood.NeighborhoodStrategy;
import cellsociety.model.grid.shape.CellShape;
import cellsociety.model.state.SugarscapeState;
import cellsociety.model.cell.WatorCell.WatorState;
import cellsociety.model.ruleset.Ruleset;
import cellsociety.model.state.CellState;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: Luke Fu
 * Updates Grid based on Cell logic
 * Does not display the grid or interact at all with javafx packages (i.e Scene, Groups, etc)
 */
public abstract class Grid {

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
  private String[] myCells;
  private List<List<Cell>> myGrid;
  private Ruleset ruleset;
  private EdgeHandler edgeHandler;
  private NeighborhoodStrategy neighborhoodStrategy;
  private CellShape cellShape;

  public void setEdgeHandler(EdgeHandler handler) {
    this.edgeHandler = handler;
  }

  public void setNeighborhoodStrategy(NeighborhoodStrategy strategy) {
    this.neighborhoodStrategy = strategy;
  }

  public void setCellShape(CellShape shape) {
    this.cellShape = shape;
  }

  /**
   * Constructor for the Grid object
   * @param rows The amount of rows we want in the grid
   * @param columns The amount of columns we want in the grid
   * @param ruleset The ruleset we want to apply to this grid
   * @param cells The initial states of the cells in a String[]
   */
  public Grid(int rows, int columns, Ruleset ruleset, String[] cells) {
    this.rows = rows;
    this.columns = columns;
    this.ruleset = ruleset;
    this.myCells = cells;
    initializeGrid();
  }

  /**
   * Initialize the grid with Cells
   */
  public void initializeGrid() {
    myGrid = new ArrayList<>();
    int count = 0;
    for (int x = 0; x < rows; x++) {
      List<Cell> row = new ArrayList<>();
      for (int y = 0; y < columns; y++) {
        CellState initialState = getInitialState(myCells[count]);
        Cell cell = CellFactory.createCell(count, initialState); // Use reflection to create cell
        row.add(cell);
        count++;
      }
      myGrid.add(row);
    }
  }

  /**
   * Function to update the grid each frame.
   */
  public void update() {
    //return a list of cell ids that were changed,
    //loop over all cells and randomly change color of alive cells with probability 0.4
    int length = getLength();
    for (int id = 0; id < length; id++) {
      int row = id / columns;
      int col = id % columns;
      Cell cell = myGrid.get(row).get(col);
      List<Cell> neighbors = getNeighbors(row, col);
      ruleset.updateCellState(cell, new ArrayList<>(neighbors));
    }
    // Second pass: Apply new states and update cells to utilize the next state
    moveNextStateToCurrent();
  }

  /**
   * Function to move all the generated next states into the current states of the cells
   */
  protected void moveNextStateToCurrent() {
    for( int id = 0; id < getLength(); id++) {
      int row = id / columns;
      int col = id % columns;
      Cell cell = myGrid.get(row).get(col);

      cell.setCurrState(cell.getNextState());
      cell.setNextState(null);
    }
  }

  /**
   * Getter to get a cell
   * @param row The row index in which the cell is contained
   * @param col The column index in which the cell is contained.
   * @return The Cell at Grid[row,col]
   */
  public Cell getCell(int row, int col) {
    return myGrid.get(row).get(col);
  }

  /**
   * Function to get the 8 neighbors around a cell
   * @param row The row index of the target cell
   * @param col The col index of a target cell
   * @return The 8 neighbors around the cell at Grid[row,col]
   */
  public List<Cell> getNeighbors(int row, int col) {
    // 1️⃣ Use cell shape to get base relative offsets
    List<int[]> neighborOffsets = cellShape.getNeighborOffsets(row, col);

    List<int[]> selectedOffsets = neighborhoodStrategy.selectNeighbors(neighborOffsets);

    return edgeHandler.handleNeighbors(row, col, selectedOffsets, this);
  }

  /**
   * Counts up the number of cells in the grid
   * @return The amount of cells in the grid
   */
  public int getLength() {
    int totalCount = 0;
    for (List<Cell> list : myGrid) {
      totalCount += list.size();
    }
    return totalCount;
  }

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

  public void expandGrid(int newRow, int newCol) {
    int rowShift = 0;
    int colShift = 0;

    // Determine if we need to shift existing cells
    if (newRow < 0) rowShift = Math.abs(newRow);
    if (newCol < 0) colShift = Math.abs(newCol);

    // Compute new grid size
    int newRows = Math.max(rows + rowShift, newRow + 1);
    int newCols = Math.max(columns + colShift, newCol + 1);

    // Create a new grid with adjusted dimensions
    List<List<Cell>> newGrid = new ArrayList<>();

    for (int i = 0; i < newRows; i++) {
      List<Cell> row = new ArrayList<>();
      for (int j = 0; j < newCols; j++) {
        if (i >= rowShift && j >= colShift && (i - rowShift) < rows && (j - colShift) < columns) {
          // Shift existing cells to their new position
          row.add(myGrid.get(i - rowShift).get(j - colShift));
        } else {
          // Create new cells with default state
          CellState initialState = ruleset.getDefaultCellState();
          Cell newCell = CellFactory.createCell(i * newCols + j, initialState);
          row.add(newCell);
        }
      }
      newGrid.add(row);
    }

    // Update grid properties
    myGrid = newGrid;
    rows = newRows;
    columns = newCols;
  }

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
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException |
             InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  public void switchEdgeHandler(String edgeClassName) {
    updateStrategy("edge", edgeClassName);
  }

  public void switchNeighborhood(String neighborhoodClassName) {
    updateStrategy("neighborhood", neighborhoodClassName);
  }

  public void switchCellShape(String shapeClassName) {
    updateStrategy("shape", shapeClassName);
  }

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
