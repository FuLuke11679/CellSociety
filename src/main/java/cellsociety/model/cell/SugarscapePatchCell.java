package cellsociety.model.cell;

public class SugarscapePatchCell {

  private int sugar;
  private int maxCapacity;
  private int growBackRate;
  private int growBackInterval;
  private int tickCounter;

  public SugarscapePatchCell(int sugar, int maxCapacity, int growBackRate, int growBackInterval) {
    this.sugar = sugar;
    this.maxCapacity = maxCapacity;
    this.growBackRate = growBackRate;
    this.growBackInterval = growBackInterval;
    this.tickCounter = 0;
  }

  public int getSugar() {
    return sugar;
  }

  public void setSugar(int sugar) {
    this.sugar = Math.min(sugar, maxCapacity);
  }

  public void growSugar() {
    tickCounter++;
    if (tickCounter >= growBackInterval) {
      sugar = Math.min(sugar + growBackRate, maxCapacity);
      tickCounter = 0;
    }
  }

  public boolean isOccupied() {
    return sugar == 0; // Example rule: patch is occupied if sugar is 0
  }
}