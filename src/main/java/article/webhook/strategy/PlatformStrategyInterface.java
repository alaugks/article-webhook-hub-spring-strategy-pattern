package article.webhook.strategy;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface PlatformStrategyInterface {

	boolean supported(String platformName);

	void handle(String rawPayload, HttpHeaders headers);

	ResponseEntity<String> getResponse();
}
