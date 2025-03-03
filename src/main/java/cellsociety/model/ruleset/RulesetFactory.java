package cellsociety.model.ruleset;

import cellsociety.model.cell.Cell;
import cellsociety.parser.Parser;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RulesetFactory {

  private static Properties myProperties;

  private static final Logger log = LogManager.getLogger(RulesetFactory.class);

  public static Ruleset createRuleset(String simName, Map<String, String> params) {
    loadPropertiesFolder();
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
      log.error("Ruleset for simulation {} could not be instantiated", simName);
      throw new RuntimeException("Ruleset for simulation " + simName + " could not be instantiated");
    }
  }

  private static void loadPropertiesFolder() {
    myProperties = new Properties();
    String propertyFileName = "cellsociety/rulesets.properties";
    try {
      InputStream myInputStream = RulesetFactory.class.getClassLoader()
          .getResourceAsStream(propertyFileName);
      myProperties.load(myInputStream);
    } catch (NullPointerException | IOException e) {
      log.error("Could not find rulesets.properties file");
    }
  }

}
