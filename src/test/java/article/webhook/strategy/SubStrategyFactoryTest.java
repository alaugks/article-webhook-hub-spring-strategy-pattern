package article.webhook.strategy;

import article.webhook.strategy.zoom.ZoomMeetingEndedSubStrategy;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class SubStrategyFactoryTest {

	private final ZoomMeetingEndedSubStrategy meetingEnded = new ZoomMeetingEndedSubStrategy();
	private SubStrategyFactory factory;

	@BeforeEach
	void beforeEach() {
		this.factory = new SubStrategyFactory(List.of(this.meetingEnded));
	}

	@Test
	void test_getAllSubStrategies() {
		assertThat(this.factory.getAllSubStrategies("zoom", "meeting.ended")).isEqualTo(List.of(this.meetingEnded));
	}

	@Test
	void test_getAllSubStrategies_not_found() {
		assertThat(this.factory.getAllSubStrategies("zoom", "unknown")).isEmpty();
		assertThat(this.factory.getAllSubStrategies("other", "meeting.ended")).isEmpty();
	}
}
