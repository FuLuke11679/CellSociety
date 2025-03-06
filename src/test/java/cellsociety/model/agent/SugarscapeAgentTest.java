package cellsociety.model.agent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import cellsociety.model.cell.SugarscapePatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SugarscapeAgentTest {

  private SugarscapeAgent agent;
  private SugarscapePatch patch;

  @BeforeEach
  void setUp() {
    agent = new SugarscapeAgent(10);
    patch = new SugarscapePatch(0, null, null, 5, 20);
  }

  @Test
  void getAgentSugar_ReturnsCorrectValue() {
    assertEquals(10, agent.getAgentSugar());
  }

  @Test
  void hasMoved_ReturnsFalseInitially() {
    assertFalse(agent.hasMoved());
  }

  @Test
  void resetMovement_SetsHasMovedToFalse() {
    agent.resetMovement();
    assertFalse(agent.hasMoved());
  }

  @Test
  void collectSugar_IncreasesSugar() {
    agent.collectSugar(patch);
    assertEquals(15, agent.getAgentSugar());
  }

  @Test
  void consumeSugar_DecreasesSugar() {
    agent.consumeSugar(3);
    assertEquals(7, agent.getAgentSugar());
  }

  @Test
  void move_AgentMovesToBetterPatch() {
    SugarscapePatch betterPatch = new SugarscapePatch(1, null, null, 15, 20);
    agent.move(java.util.List.of(betterPatch), patch, 3, 2);
    assertEquals(23, agent.getAgentSugar()); // 10 initial + 15 collected, 2 consumed = 23
  }
}
