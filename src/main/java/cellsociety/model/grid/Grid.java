package cellsociety.model.grid;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.ConwayCell.ConwayState;
import cellsociety.model.cell.FireCell.FireState;
import cellsociety.model.cell.PercolationCell.PercolationState;
import cellsociety.model.cell.SegregationCell.SegregationState;
import cellsociety.model.cell.WatorCell.WatorState;
import cellsociety.model.ruleset.Ruleset;
import cellsociety.model.state.CellState;
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
      Map.entry("W", WatorState.WATER)
  );

  private int rows;
  private int columns;
  private String[] myCells;
  private List<List<Cell>> myGrid;
  private Ruleset ruleset;

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
        String cellType = getCellTypeForState(initialState); // Map state to cell type name
        Cell cell = createCell(count, initialState, null, cellType); // Use reflection to create cell
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
    List<Cell> neighbors = new ArrayList<>();
    int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
    int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

    for (int i = 0; i < 8; i++) {
      int newRow = row + dx[i];
      int newCol = col + dy[i];

      // ✅ Ensures newRow and newCol are within bounds before adding a neighbor
      if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < columns) {
        neighbors.add(myGrid.get(newRow).get(newCol));
      }
    }
    return neighbors;
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

  public Grid getGrid() {
    return this;
  }

  protected Ruleset getRuleset() {
    return ruleset;
  }

  public Cell createCell(int id, CellState currState, CellState nextState, String cellType) {
    try {
      // Construct the full class name by using the cellType
      Class<?> cellClass = Class.forName("cellsociety.model.cell." + cellType);

      // Return an instance of the correct cell type
      return (Cell) cellClass.getConstructor(int.class, CellState.class, CellState.class)
          .newInstance(id, currState, nextState);
    } catch (Exception e) {
      e.printStackTrace();
      return null;  // Handle error or return a default cell if necessary
    }
  }

  private String getCellTypeForState(CellState state) {
    if (state instanceof ConwayState) {
      return "ConwayCell";
    } else if (state instanceof FireState) {
      return "FireCell";
    } else if (state instanceof PercolationState) {
      return "PercolationCell";
    } else if (state instanceof WatorState) {
      return "WatorCell";
    } else if (state instanceof SegregationState) {
      return "SegregationCell";
    }
    return "Cell";
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
