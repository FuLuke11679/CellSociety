package cellsociety.model.factory;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.agent.WatorAgent;
import cellsociety.model.cell.WatorCell.WatorState;
import java.util.HashMap;
import java.util.Map;
import javax.management.RuntimeErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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