package cellsociety.model.cell;

import cellsociety.model.state.CellState;

public class SugarscapePatchCell extends Cell {

  public enum SugarscapePatchState implements CellState {
    EMPTY,
    LOW_SUGAR,
    MEDIUM_SUGAR,
    HIGH_SUGAR
  }

  private int sugar;
  private int maxCapacity;
  private int growBackRate;
  private int growBackInterval;
  private int tickCounter;

  public SugarscapePatchCell(int id, CellState currState, CellState nextState, int sugar, int maxCapacity, int growBackRate, int growBackInterval) {
    super(id, currState, nextState);
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