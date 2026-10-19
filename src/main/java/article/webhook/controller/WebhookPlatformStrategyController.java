package article.webhook.controller;

import article.webhook.strategy.PlatformStrategyFactory;
import article.webhook.strategy.PlatformStrategyInterface;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.core.JacksonException;

@RestController
public class WebhookPlatformStrategyController {

	private final PlatformStrategyFactory platformStrategyFactory;
	public WebhookPlatformStrategyController(PlatformStrategyFactory platformStrategyFactory) {
		this.platformStrategyFactory = platformStrategyFactory;
	}

	@PostMapping(
		value = {
			"/{platform:[a-z]{1,16}}"
		}
	)
	public ResponseEntity<String> save(
		@PathVariable String platform,
		@RequestBody(required = false) String requestBody,
		@RequestHeader HttpHeaders headers
	) {
		// Check strategy available
		PlatformStrategyInterface strategy = platformStrategyFactory.getPlatformStrategy(platform);

		if (strategy == null) {
			return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.contentType(MediaType.APPLICATION_JSON)
				.body(String.format("{\"error\":\"PlatformStrategy [%s] not found\"}", platform));
		}

		try {
			strategy.handle(requestBody, headers);
		} catch (JacksonException e) {
			return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.contentType(MediaType.APPLICATION_JSON)
				.body(String.format("{\"error\":\"PlatformStrategy [%s] with JSON error\"}", platform));
		} catch (Exception e) {
			return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.contentType(MediaType.APPLICATION_JSON)
				.body(String.format("{\"error\":\"PlatformStrategy [%s] handle failed\"}", platform));
		}

        return strategy.getResponse();
	}
}
