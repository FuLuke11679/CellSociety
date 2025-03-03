package cellsociety.model.state;

import java.util.HashMap;
import java.util.Map;

public class StateHandlerFactory {

  private static final Map<String, CellStateHandler> handlerMap = new HashMap<>();

  static {
    handlerMap.put("Conway", new ConwayStateHandler());
    handlerMap.put("Fire", new FireStateHandler());
    handlerMap.put("Percolation", new PercolationStateHandler());
    handlerMap.put("Segregation", new SegregationStateHandler());
    handlerMap.put("Wator", new WatorStateHandler());
  }

  public static CellStateHandler getHandler(String simulationType) {
    return handlerMap.get(simulationType);
  }

}
