package article.webhook.strategy;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SubStrategyFactory {

	private final List<SubStrategyInterface> strategies;

	public SubStrategyFactory(List<SubStrategyInterface> strategies) {
		this.strategies = strategies;
	}

	public List<SubStrategyInterface> getAllSubStrategies(String platformName, String eventName) {
		return strategies.stream()
			.filter(strategy -> strategy.supported(platformName, eventName))
			.toList();
	}
}
