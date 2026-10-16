package article.webhook.strategy;

import java.util.Map;
import java.util.Set;

public abstract class SubStrategyAbstract implements SubStrategyInterface {

	private final Map<String, Set<String>> supported;

    protected SubStrategyAbstract(String platformName, String eventName) {
        this.supported = Map.of(platformName, Set.of(eventName));
    }

	protected SubStrategyAbstract(Map<String, Set<String>> supported) {
		this.supported = supported;
	}

	@Override
	public boolean supported(String platformName, String eventName) {
		return supported.getOrDefault(platformName, Set.of()).contains(eventName);
	}
}
