package article.webhook.strategy;

import java.util.Map;
import java.util.Set;

public abstract class SubStrategyAbstract implements SubStrategyInterface {

	private final Map<String, Set<String>> supported;

    protected SubStrategyAbstract(String platform, String event) {
        this.supported = Map.of(platform, Set.of(event));
    }

	protected SubStrategyAbstract(Map<String, Set<String>> supported) {
		this.supported = supported;
	}

	@Override
	public boolean supported(String platform, String event) {
		return supported.getOrDefault(platform, Set.of()).contains(event);
	}
}
