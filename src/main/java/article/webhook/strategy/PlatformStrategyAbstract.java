package article.webhook.strategy;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class PlatformStrategyAbstract implements PlatformStrategyInterface {

	private final String platform;

    protected PlatformStrategyAbstract(
        String platform
	) {
        this.platform = platform;
    }

	@Override
	public boolean supported(String platform) {
		return this.platform.equals(platform);
	}

	@Override
	public ResponseEntity<String> getResponse() {
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
