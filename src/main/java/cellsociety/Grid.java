package cellsociety;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
import java.util.Random;
import cellsociety.cell.GameOfLifeCell;
import cellsociety.cell.GameOfLifeCell.State;
import cellsociety.ruleset.GameOfLifeRuleset;
/*
Updates Grid based on Cell logic
Does not display the grid or interact at all with javafx packages (i.e Scene, Groups, etc)
 */
public class Grid {
  private int rows;
  private int columns;
  private List<List<GameOfLifeCell>> myGrid;
  private GameOfLifeRuleset ruleset;

  /**
   * Constructor for GridManager.
   *
   * @param rows       Number of rows in the grid.
   * @param columns    Number of columns in the grid.
   */
  public Grid(int rows, int columns) {
    this.rows = rows;
    this.columns = columns;
    this.ruleset = new GameOfLifeRuleset();
    myGrid = new ArrayList<>();
    initializeGrid();
  }

  /**
   * Initialize the grid with Cells
   */
  public void initializeGrid() {
    //myGrid = new ArrayList<>();
    int count = 0;
    for (int x = 0; x < rows; x++) {
      List<GameOfLifeCell> row = new ArrayList<>();
      for (int y = 0; y < columns; y++) {
        //Randomly set cells as ALIVE or DEAD
        State initialState = Math.random() < 0.2 ? State.ALIVE : State.DEAD;
        row.add(new GameOfLifeCell(count, State.DEAD, initialState));
        count++;
      }
      myGrid.add(row);
    }
  }

  public List<Integer> update(){
    //return a list of cell ids that were changed,
    //loop over all cells and randomly change color of alive cells with probability 0.2
    List<Integer> updatedCells = new ArrayList<>();
    int length = getLength();
    for (int id = 0; id < length; id++) {
      int row = id / columns;
      int col = id % columns;
      GameOfLifeCell cell = myGrid.get(row).get(col);
      List<GameOfLifeCell> neighbors = getNeighbors(row, col);
      ruleset.updateState(cell, new ArrayList<>(neighbors));
    }
    // Second pass: Apply new states and collect updates
    for (int x = 0; x < rows; x++) {
      for (int y = 0; y < columns; y++) {
        GameOfLifeCell cell = myGrid.get(x).get(y);
        if (cell.getPrevState() != cell.getCurrState()) {
          cell.setColor(cell.getCurrState() == State.ALIVE ? Color.BLACK : Color.WHITE);
          updatedCells.add(cell.getId());
        }
      }
    }

    return updatedCells;
  }
  public Color getColor(int row, int col){
    return myGrid.get(row).get(col).getColor();
  }

  private List<GameOfLifeCell> getNeighbors(int row, int col) {
    List<GameOfLifeCell> neighbors = new ArrayList<>();
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
    for (List<GameOfLifeCell> list : myGrid) {
      totalCount += list.size();
    }
    return totalCount;
  }

}
