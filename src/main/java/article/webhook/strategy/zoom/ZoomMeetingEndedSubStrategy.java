package article.webhook.strategy.zoom;

import article.webhook.strategy.Payload;
import article.webhook.strategy.SubStrategyAbstract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ZoomMeetingEndedSubStrategy extends SubStrategyAbstract {

	private static final Logger LOGGER = LoggerFactory.getLogger(ZoomMeetingEndedSubStrategy.class);

	public static final String STRATEGY = "zoom";
	public static final String EVENT = "meeting.ended";

	public ZoomMeetingEndedSubStrategy() {
		super(STRATEGY, EVENT);
	}

	@Override
	public void handle(Payload payload) {
		// Log only to illustrate the flow when testing locally
		LOGGER.info("{} handled {} / {}", getClass().getSimpleName(), payload.platform(), payload.event());
	}
}
