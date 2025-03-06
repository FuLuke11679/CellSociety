package cellsociety.parser;

import cellsociety.model.factory.RulesetFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PropertiesLoader {

  private static final Logger log = LogManager.getLogger(PropertiesLoader.class);

  public static void loadPropertiesFolder(String propertyFileName, Properties props) {
    try {
      InputStream myInputStream = RulesetFactory.class.getClassLoader()
          .getResourceAsStream(propertyFileName);
      props.load(myInputStream);
    } catch (NullPointerException | IOException e) {
      log.error("Could not find designated properties file");
    }
  }

}
