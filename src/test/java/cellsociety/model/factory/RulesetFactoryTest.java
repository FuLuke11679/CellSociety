package cellsociety.model.factory;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.ruleset.Ruleset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Author: Daniel Rodriguez-Florido
 *
 * Testing file for the RulesetFactory object
 */
class RulesetFactoryTest {

  private Map<String, String> params;
  private List<String> simNames;

  @BeforeEach
  void setUp() {
    simNames = List.of(
        "Conway",
        "Fire",
        "Percolation",
        "Segregation",
        "WatorWorld",
        "GeneralConway"
    );
  }

  @Test
  void createRuleset_noParams_success() {
    params = new HashMap<>();
    Ruleset r = RulesetFactory.createRuleset("Conway", params);
    assertNotNull(r);
  }

  @Test
  void createRuleset_withValidParams_success() {
    params = Map.of(
        "probGrow", "0.03",
        "probCatch", "0.003"
    );
    Ruleset r = RulesetFactory.createRuleset("Fire", params);
    assertNotNull(r);
  }

  @Test
  void createRuleset_withInvalidParams_success() {
    params = Map.of(
        "VDKJALKJ", "3",
        "ASDKJKAJ", "100"
    );
    // Should fill in default parameters anyway
    for (String s : simNames) {
      Ruleset r = RulesetFactory.createRuleset(s, params);
      assertNotNull(r);
    }
  }

  @Test
  void createRuleset_withInvalidSimNameNoParams_fail() { // Negative Test
    params = new HashMap<>();
    assertThrows(RuntimeException.class, () ->
      RulesetFactory.createRuleset("Gibberish", params)
    );
  }

  @Test
  void createRuleset_invalidSimNameWithParams_fail() {
    params = Map.of(
        "probGrow", "0.03",
        "probCatch", "0.003"
    );
    assertThrows(RuntimeException.class, () ->
        RulesetFactory.createRuleset("Gibberish", params)
    );
  }

}