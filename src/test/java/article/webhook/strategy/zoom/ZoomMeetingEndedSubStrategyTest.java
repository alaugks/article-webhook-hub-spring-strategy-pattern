package article.webhook.strategy.zoom;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class ZoomMeetingEndedSubStrategyTest {

	private final ZoomMeetingEndedSubStrategy strategy = new ZoomMeetingEndedSubStrategy();

	@Test
	void test_supported() {
		assertThat(this.strategy.supported("zoom", "meeting.ended")).isTrue();
		assertThat(this.strategy.supported("zoom", "meeting.started")).isFalse();
		assertThat(this.strategy.supported("gohighlevel", "meeting.ended")).isFalse();
	}

	// Further tests
}
