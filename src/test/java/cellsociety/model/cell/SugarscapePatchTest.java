package cellsociety.model.cell;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.agent.SugarscapeAgent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SugarscapePatchTest {
  private SugarscapePatch patch;
  private SugarscapeAgent agent;

  @BeforeEach
  void setUp() {
    patch = new SugarscapePatch(0, null,null, 10, 20);
    agent = new SugarscapeAgent(15);
  }

  @Test
  void getSugarAmount_ReturnsCorrectValue() {
    assertEquals(10, patch.getSugarAmount());
  }

  @Test
  void getMaxSugar_ReturnsCorrectValue() {
    assertEquals(20, patch.getMaxSugar());
  }

  @Test
  void hasAgent_ReturnsFalseInitially() {
    assertFalse(patch.hasAgent());
  }

  @Test
  void getAgent_ReturnsNullInitially() {
    assertNull(patch.getAgent());
  }

  @Test
  void setAgent_AssignsAgentCorrectly() {
    patch.setAgent(agent);
    assertTrue(patch.hasAgent());
    assertEquals(agent, patch.getAgent());
  }

  @Test
  void removeAgent_RemovesAgentCorrectly() {
    patch.setAgent(agent);
    patch.removeAgent();
    assertFalse(patch.hasAgent());
    assertNull(patch.getAgent());
  }

  @Test
  void growSugar_IncreasesSugarButNotBeyondMax() {
    patch.growSugar(5);
    assertEquals(15, patch.getSugarAmount());

    patch.growSugar(10);
    assertEquals(20, patch.getSugarAmount()); // Should cap at maxSugar
  }

  @Test
  void harvestSugar_SetsSugarToZero() {
    patch.harvestSugar();
    assertEquals(0, patch.getSugarAmount());
  }
}
