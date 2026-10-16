package article.webhook.fixtures;

import article.webhook.strategy.PlatformStrategyAbstract;
import org.springframework.http.HttpHeaders;

public class TestStrategyDummy extends PlatformStrategyAbstract {

	public TestStrategyDummy() {
		super("foobar");
	}

	@Override
	public void handle(String rawPayload, HttpHeaders headers) {

	}
}
