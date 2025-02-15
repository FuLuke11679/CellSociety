package cellsociety.model.grid;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.ConwayCell.ConwayState;
import cellsociety.model.cell.FireCell.FireState;
import cellsociety.model.cell.PercolationCell.PercolationState;
import cellsociety.model.cell.SegregationCell.SegregationState;
import cellsociety.model.cell.WatorCell.WatorState;
import cellsociety.model.ruleset.ConwayRuleset;
import cellsociety.model.ruleset.Ruleset;
import cellsociety.model.state.CellState;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
Updates Grid based on Cell logic
Does not display the grid or interact at all with javafx packages (i.e Scene, Groups, etc)
 */
public abstract class Grid {

  protected final static Map<String, CellState> stateMap = Map.ofEntries(
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

  protected int rows;
  protected int columns;
  protected String[] myCells;
  protected List<List<Cell>> myGrid;
  protected Ruleset ruleset;

  /**
   * Constructor for GridManager.
   *
   * @param rows    Number of rows in the grid.
   * @param columns Number of columns in the grid.
   */
  public Grid(int rows, int columns) {
    this.rows = rows;
    this.columns = columns;
    this.ruleset = new ConwayRuleset();
    myGrid = new ArrayList<>();
    initializeGrid();
  }

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


  public void update() {
    //return a list of cell ids that were changed,
    //loop over all cells and randomly change color of alive cells with probability 0.4
    int length = getLength();
    for (int id = 0; id < length; id++) {
      int row = id / columns;
      int col = id % columns;
      Cell cell = myGrid.get(row).get(col);
      List<Cell> neighbors = getNeighbors(row, col);
      ruleset.updateState(cell, new ArrayList<>(neighbors));
    }
    // Second pass: Apply new states and update cells to utilize the next state
    for( int id = 0; id < length; id++) {
      int row = id / columns;
      int col = id % columns;
      Cell cell = myGrid.get(row).get(col);
      if(cell.getNextState() != null && cell.getNextState() != cell.getCurrState()){
        cell.setCurrState(cell.getNextState());
        cell.setNextState(null);
      }
    }
  }


  public Cell getCell(int row, int col) {
    return myGrid.get(row).get(col);
  }

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

  public int getLength() {
    int totalCount = 0;
    for (List<Cell> list : myGrid) {
      totalCount += list.size();
    }
    return totalCount;
  }

  protected CellState getInitialState(String stateSymbol) {
    return stateMap.get(stateSymbol);
  }

  public int getRows() {
    return rows;
  }

  public int getColumns() {
    return columns;
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


}
