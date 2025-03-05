package cellsociety.model.factory;

import cellsociety.model.agent.WatorAgent;
import cellsociety.model.state.CellState;
import cellsociety.parser.PropertiesLoader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Author: Daniel Rodriguez-Florido
 *
 * Agent factory for the WatorWorld Ruleset (instantiates either fish or shark)
 */
public class WatorAgentFactory {

  private static final String PROPERTY_FILE_NAME = "cellsociety/agents.properties";

  private static final String FISH_BREED_PARAM_NAME = "fishBreedTime";
  private static final String SHARK_BREED_PARAM_NAME = "sharkBreedTime";
  private static final String SHARK_ENERGY_GAIN_NAME = "sharkEnergyGain";

  private static final Logger log = LogManager.getLogger(WatorAgentFactory.class);

  private final Map<String, String> params;

  /**
   * Constructor to take in the params of the agents
   * @param params The parameters we wish to fill the agents with
   */
  public WatorAgentFactory(Map<String, String> params) {
    this.params = params;
  }

  /**
   * Creates a new WatorAgent based on the state of the cell
   * @param agentType WatorState indicating The type of agent we wish to create
   * @return The new WatorAgent
   */
  public WatorAgent createWatorAgent(CellState agentType) {
    Properties myProperties = new Properties();
    PropertiesLoader.loadPropertiesFolder(PROPERTY_FILE_NAME, myProperties);
    String agentClassName = myProperties.getProperty(agentType.toString());

    try {
      Class<?> clazz = Class.forName(agentClassName);

      try {
        Constructor<?> constructor = clazz.getConstructor(int.class, int.class);
        int param1 = Integer.parseInt(params.getOrDefault(SHARK_ENERGY_GAIN_NAME, "5"));
        int param2 = Integer.parseInt(params.getOrDefault(SHARK_BREED_PARAM_NAME, "3"));
        return (WatorAgent) constructor.newInstance(param1, param2);
      } catch (NoSuchMethodException e) {
        log.warn("No constructor for agent of two params. Trying other constructor.");
      } catch (Exception e) {
        log.error("Could not instantiate Agent with two parameters: {}", agentType);
      }

      Constructor<?> constructor = clazz.getConstructor(int.class);
      int param1 = Integer.parseInt(params.getOrDefault(FISH_BREED_PARAM_NAME, "5"));
      return (WatorAgent) constructor.newInstance(param1);

    } catch (ClassNotFoundException | NoSuchMethodException e) {
      log.error("Could not load agent class {}", agentClassName);
      throw new RuntimeException("Could not load agent class " + agentClassName);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      log.error("Could not instantiate Agent with one parameter: {}", agentType);
      throw new RuntimeException("Could not instantiate Agent with one parameter " + agentType);
    }

  }

}
