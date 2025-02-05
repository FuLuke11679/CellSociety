package cellsociety;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
import java.util.Random;

/*
Updates Grid based on Cell logic
Does not display the grid or interact at all with javafx packages (i.e Scene, Groups, etc)
 */
public class Grid {
  private GridView gridView;
  private int rows;
  private int columns;
  private List<List<CellUnit>> myGrid;

  /**
   * Constructor for GridManager.
   *
   * @param rows       Number of rows in the grid.
   * @param columns    Number of columns in the grid.
   */
  public Grid(int rows, int columns, List<CellUnit> initStates) {
    this.rows = rows;
    this.columns = columns;
    myGrid = new ArrayList<>();
    initializeGrid(initStates);
  }

  /**
   * Initialize the grid with Cells
   */
  public void initializeGrid(List<CellUnit> initStates) {
    //myGrid = new ArrayList<>();
    int count = 0;
    for (int x = 0; x < rows; x++) {
      List<CellUnit> row = new ArrayList<>();
      for (int y = 0; y < columns; y++) {
        row.add(initStates.get(count));
        count++;
      }
      myGrid.add(row);
    }
  }
  //SHOULDN'T HAVE THIS
  public List<List<CellUnit>> getGrid(){
    return myGrid;
  }

  public List<Integer> update(){
    //return a list of cell ids that were changed,
    //loop over all cells and randomly change color of alive cells with probability 0.2
    List<Integer> updatedList = new ArrayList<>();
    Random random = new Random();
    for (int x = 0; x < rows; x+=2) {
      List<CellUnit> row = myGrid.get(x);
      for (int y = 0; y < columns; y+=2) {
        CellUnit currCell = row.get(y);
        if(random.nextDouble()<0.2){
          currCell.setColor(Color.BLUE);
        }
        else {
          currCell.setColor(Color.WHITE);
        }
        updatedList.add(currCell.getID());
      }
    }
    return updatedList;
  }
  public Color getColor(int row, int col){
    return myGrid.get(row).get(col).getColor();
  }
  public int getLength(){
    int totalCount = 0;
    for (List<CellUnit> list : myGrid) {
      totalCount += list.size();
    }
    return totalCount;
  }

}
