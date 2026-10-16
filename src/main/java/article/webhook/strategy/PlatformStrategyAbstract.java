package article.webhook.strategy;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class PlatformStrategyAbstract implements PlatformStrategyInterface {

	private final String platformName;

    protected PlatformStrategyAbstract(
        String platformName
	) {
        this.platformName = platformName;
    }

	@Override
	public boolean supported(String platformName) {
		return this.platformName.equals(platformName);
	}

	@Override
	public ResponseEntity<String> getResponse() {
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
