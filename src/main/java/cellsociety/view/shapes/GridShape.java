package cellsociety.view.shapes;

import javafx.scene.shape.Shape;

public interface GridShape {
  Shape createShape(int size, int row, int col);
}
