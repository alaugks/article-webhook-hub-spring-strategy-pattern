package article.webhook.service;

import article.webhook.strategy.Payload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RawDataSaveService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RawDataSaveService.class);

	/**
	 * Logs at INFO level only to make local testing easy (see README). Replaces the real persistence.
	 * One line per request: platform; event; rawData
	 */
	public void save(Payload payload) {
		LOGGER.info("{}; {}; {}", payload.platform(), payload.event(), payload.rawData().replaceAll("\\R", " "));
	}

}
