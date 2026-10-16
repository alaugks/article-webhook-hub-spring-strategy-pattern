package article.webhook.strategy;

import article.webhook.strategy.foundry.FoundrySubStrategy;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class FoundrySubStrategyTest {

	private final FoundrySubStrategy strategy = new FoundrySubStrategy();

	@Test
	void test_supported() {
		assertThat(this.strategy.supported("zoom", "meeting.ended")).isTrue();
		assertThat(this.strategy.supported("zoom", "meeting.started")).isTrue();
	}

	@Test
	void test_not_supported_event() {
		assertThat(this.strategy.supported("zoom", "recording.completed")).isFalse();
	}

	@Test
	void test_not_supported_strategy() {
		assertThat(this.strategy.supported("gohighlevel", "meeting.ended")).isFalse();
	}
}
