package cellsociety.model;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.FireCell;
import cellsociety.model.cell.FireCell.FireState;
import cellsociety.model.cell.PercolationCell;
import cellsociety.model.cell.PercolationCell.PercolationState;
import cellsociety.model.ruleset.Ruleset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import cellsociety.model.cell.ConwayCell;
import cellsociety.model.cell.ConwayCell.ConwayState;
import cellsociety.model.state.CellState;
import cellsociety.model.ruleset.ConwayRuleset;
/*
Updates Grid based on Cell logic
Does not display the grid or interact at all with javafx packages (i.e Scene, Groups, etc)
 */
public class Grid {

  private final static Map<String, CellState> stateMap = Map.of(
      "A", ConwayState.ALIVE,
      "D", ConwayState.DEAD,
      "B", FireState.BURNING,
      "T", FireState.TREE,
      "E", FireState.EMPTY,
      "BL", PercolationState.BLOCKED,
      "P", PercolationState.PERCOLATED,
      "O", PercolationState.OPEN
  );

  private int rows;
  private int columns;
  private String[] myCells;
  private List<List<Cell>> myGrid;
  private Ruleset ruleset;

  /**
   * Constructor for GridManager.
   *
   * @param rows       Number of rows in the grid.
   * @param columns    Number of columns in the grid.
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
        CellState initialState = stateMap.get(myCells[count]);
        // Use the appropriate cell type based on the state
        Cell cell;
        if (initialState instanceof ConwayState) {
          cell = new ConwayCell(count, null, initialState);
        } else if (initialState instanceof FireState) {
          cell = new FireCell(count, null, initialState);  // Update with FireCell constructor
        } else if (initialState instanceof PercolationState) {
          cell = new PercolationCell(count, null, initialState);  // Similarly for PercolationCell
        } else {
          throw new IllegalArgumentException("Unsupported state: " + initialState);
        }
        row.add(cell);
        count++;
      }
      myGrid.add(row);
    }
  }


  public void update(){
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


  public Cell getCell(int row, int col){
    return myGrid.get(row).get(col);
  }

  private List<Cell> getNeighbors(int row, int col) {
    List<Cell> neighbors = new ArrayList<>();
    int[] dx = {-1, -1, -1,  0,  0,  1,  1,  1};
    int[] dy = {-1,  0,  1, -1,  1, -1,  0,  1};

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

  public int getLength(){
    int totalCount = 0;
    for (List<Cell> list : myGrid) {
      totalCount += list.size();
    }
    return totalCount;
  }

}
