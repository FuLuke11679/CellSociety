package cellsociety.model.factory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cellsociety.model.agent.WatorAgent;
import cellsociety.model.cell.WatorCell.WatorState;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Author: Daniel Rodriguez-Florido
 *
 * Testing file for the WatorAgentFactory object
 */
class WatorAgentFactoryTest {

  Map<String, String> params;
  WatorAgentFactory factory;

  @BeforeEach
  void setUp() {
    params = Map.of(
        "fishBreedTime", "10",
        "sharkEnergyGain", "5",
        "sharkBreedTime", "3"
    );
    factory = new WatorAgentFactory(params);
  }

  @Test
  void createWatorAgent_createFish_success() {
    WatorAgent agent = factory.createWatorAgent(WatorState.FISH);
    assertNotNull(agent);
  }

  @Test
  void createWatorAgent_createShark_success() {
    WatorAgent agent = factory.createWatorAgent(WatorState.SHARK);
    assertNotNull(agent);
  }

  @Test
  void createWatorAgent_wrongInputState_failure() { // Negative test
    assertThrows(RuntimeException.class, () -> factory.createWatorAgent(WatorState.WATER));
  }

}