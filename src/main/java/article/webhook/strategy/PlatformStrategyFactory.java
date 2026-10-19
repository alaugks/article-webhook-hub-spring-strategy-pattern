package article.webhook.strategy;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PlatformStrategyFactory {

	/** Collection Injection */
	private final List<PlatformStrategyInterface> strategies;

	public PlatformStrategyFactory(List<PlatformStrategyInterface> platformStrategies) {
		this.strategies = platformStrategies;
	}

	public PlatformStrategyInterface getPlatformStrategy(String platform) {
		for (PlatformStrategyInterface strategy : strategies) {
			if (strategy.supported(platform)) {
				return strategy;
			}
		}

		return null;
	}
}
