package cellsociety.model.agent;

import cellsociety.model.state.CellState;
import cellsociety.parser.PropertiesLoader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WatorAgentFactory {

  private static final String PROPERTY_FILE_NAME = "cellsociety/agents.properties";

  private static final String FISH_BREED_PARAM_NAME = "fishBreedTime";
  private static final String SHARK_BREED_PARAM_NAME = "sharkBreedTime";
  private static final String SHARK_ENERGY_GAIN_NAME = "sharkEnergyGain";

  private static final Logger log = LogManager.getLogger(WatorAgentFactory.class);

  private final Map<String, String> params;

  public WatorAgentFactory(Map<String, String> params) {
    this.params = params;
  }

  public WatorAgent createWatorAgent(CellState agentType) {
    Properties myProperties = new Properties();
    PropertiesLoader.loadPropertiesFolder(PROPERTY_FILE_NAME, myProperties);
    String agentClassName = myProperties.getProperty(agentType.toString());

    try {
      Class<?> clazz = Class.forName(agentClassName);

      try {
        Constructor<?> constructor = clazz.getConstructor(int.class, int.class);
        int param1 = Integer.parseInt(params.get(SHARK_ENERGY_GAIN_NAME));
        int param2 = Integer.parseInt(params.get(SHARK_BREED_PARAM_NAME));
        return (WatorAgent) constructor.newInstance(param1, param2);
      } catch (NoSuchMethodException e) {
        log.error("No constructor for ruleset.");
      } catch (Exception e) {
        log.error("Could not instantiate Agent with two parameters: {}", agentType);
      }

      Constructor<?> constructor = clazz.getConstructor(int.class);
      int param1 = Integer.parseInt(params.get(FISH_BREED_PARAM_NAME));
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
