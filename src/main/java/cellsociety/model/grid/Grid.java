package cellsociety.model.grid;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.ConwayCell.ConwayState;
import cellsociety.model.cell.FireCell.FireState;
import cellsociety.model.cell.PercolationCell.PercolationState;
import cellsociety.model.cell.SegregationCell.SegregationState;
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
  public abstract void initializeGrid();


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
      if(cell.getCurrState() != cell.getNextState()){
        cell.setCurrState(cell.getNextState());
        cell.setNextState(null);
      }
    }
    // Second pass: Apply new states and collect updates
//    for (int x = 0; x < rows; x++) {
//      for (int y = 0; y < columns; y++) {
//        Cell cell = myGrid.get(x).get(y);
//        if (cell.getPrevState() != cell.getCurrState()) {
//          cell.setColor(cell.getCurrState() == ConwayState.ALIVE ? Color.BLACK : Color.WHITE);
//        }
//      }
//    }
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
