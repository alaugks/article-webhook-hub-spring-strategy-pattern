package article.webhook.strategy.foundry;

import article.webhook.strategy.Payload;
import article.webhook.strategy.SubStrategyAbstract;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FoundrySubStrategy extends SubStrategyAbstract {

	private static final Logger LOGGER = LoggerFactory.getLogger(FoundrySubStrategy.class);

	// strategy -> events this sub strategy is responsible for
	private static final Map<String, Set<String>> SUPPORTED = Map.of(
		"zoom", Set.of("meeting.ended", "meeting.started")
	);

	public FoundrySubStrategy() {
		super(SUPPORTED);
	}

	@Override
	public void handle(Payload payload) {
		// Log only to illustrate the flow when testing locally (no real forwarding to Foundry)
		LOGGER.info("{} handled {} / {}", getClass().getSimpleName(), payload.platform(), payload.event());
	}
}
