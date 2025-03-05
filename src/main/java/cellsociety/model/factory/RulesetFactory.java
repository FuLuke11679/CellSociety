package cellsociety.model.factory;

import cellsociety.model.ruleset.Ruleset;
import cellsociety.parser.PropertiesLoader;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RulesetFactory {

  private static final String PROPERTY_FILE_NAME = "cellsociety/rulesets.properties";

  private static final Logger log = LogManager.getLogger(RulesetFactory.class);

  public static Ruleset createRuleset(String simName, Map<String, String> params) {
    Properties myProperties = new Properties();
    PropertiesLoader.loadPropertiesFolder(PROPERTY_FILE_NAME, myProperties);
    String rulesetClass = myProperties.getProperty(simName);

    try {
      Class<? extends Ruleset> clazz = (Class<? extends Ruleset>) Class.forName(rulesetClass);

      if (params != null && !params.isEmpty()) {
        try {
          Constructor<? extends Ruleset> constructor = clazz.getConstructor(Map.class);
          return constructor.newInstance(params);
        } catch (NoSuchMethodException e) {
          log.error("No constructor for ruleset.");
        }
      }

      Constructor<? extends Ruleset> defaultConstructor = clazz.getDeclaredConstructor();
      return (Ruleset) defaultConstructor.newInstance();
    } catch(NoSuchMethodException | ClassNotFoundException e) {
      log.error("Ruleset {} not found", rulesetClass);
      throw new RuntimeException("Ruleset " + rulesetClass + " not found");
    } catch (Exception e) {
      log.error("Ruleset for simulation {} could not be instantiated --- {}", simName, rulesetClass);
      throw new RuntimeException("Ruleset for simulation " + simName + " could not be instantiated");
    }
  }

}
