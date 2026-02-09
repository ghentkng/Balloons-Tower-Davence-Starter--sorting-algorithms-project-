//Fairly self explanatory class

import java.util.List;

@FunctionalInterface
public interface TargetingStrategy {
    Balloon chooseTarget(Tower tower, List<Balloon> balloons);
}
