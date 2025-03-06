package cellsociety.model.agent;

import cellsociety.model.cell.SugarscapePatch;
import java.util.List;

public class SugarscapeAgent {

  private int agentSugar;
  private boolean alreadyMoved;

  public SugarscapeAgent(int initialSugar) {
    this.agentSugar = initialSugar;
    this.alreadyMoved = false;
  }

  public int getAgentSugar() {
    return agentSugar;
  }

  public boolean hasMoved() {
    return alreadyMoved;
  }

  public void resetMovement() {
    alreadyMoved = false;
  }

  public void collectSugar(SugarscapePatch patch) {
    agentSugar += patch.getSugarAmount();
    patch.harvestSugar();
  }

  public void consumeSugar(int metabolism) {
    agentSugar -= metabolism;
    if (agentSugar <= 0) {
      agentSugar = 0;
    }
  }

  public void move(List<SugarscapePatch> visionPatches, SugarscapePatch currentPatch, int vision,
      int metabolism) {
    if (alreadyMoved) {
      return;
    }
    SugarscapePatch bestPatch = findBestPatch(visionPatches);
    if (bestPatch != null) {
      collectSugar(bestPatch);
      currentPatch.removeAgent();
      bestPatch.setAgent(this);
      consumeSugar(metabolism);
    }
    alreadyMoved = true;
  }

  private SugarscapePatch findBestPatch(List<SugarscapePatch> patches) {
    SugarscapePatch best = null;
    int maxSugar = -1;
    for (SugarscapePatch patch : patches) {
      if (!patch.hasAgent() && patch.getSugarAmount() > maxSugar) {
        best = patch;
        maxSugar = patch.getSugarAmount();
      }
    }
    return best;
  }
}
