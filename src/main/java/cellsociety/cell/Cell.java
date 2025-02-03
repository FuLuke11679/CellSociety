package cellsociety.cell;

public abstract class Cell {

  private final int id;

  public Cell(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

}
