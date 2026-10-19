package article.webhook.strategy;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SubStrategyFactory {

	private final List<SubStrategyInterface> strategies;

	public SubStrategyFactory(List<SubStrategyInterface> strategies) {
		this.strategies = strategies;
	}

	public List<SubStrategyInterface> getAllSubStrategies(String platform, String event) {
		return strategies.stream()
			.filter(strategy -> strategy.supported(platform, event))
			.toList();
	}
}
