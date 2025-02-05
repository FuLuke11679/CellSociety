package cellsociety;

import java.util.Random;
import javafx.scene.paint.Color;

public class CellUnit {
    private int id;
    private Color color;


    public CellUnit(int id, double density) {
      this.id = id;
      Random rand = new Random();
      if(rand.nextDouble() < density){
        this.color = Color.BLUE;
      }
      else{
        this.color = Color.WHITE;
      }
    }

  public CellUnit(int id, Color color){
    this.id = id;
    this.color = color;
  }


    public int getID(){
      return id;
    }
    public Color getColor(){
      return color;
    }
    public void setColor(Color color){
      this.color = color;
    }

}


