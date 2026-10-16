package article.webhook.service;

import article.webhook.strategy.Payload;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

class RawDataSaveServiceTest {

	private final Logger logger = (Logger) LoggerFactory.getLogger(RawDataSaveService.class);
	private final ListAppender<ILoggingEvent> appender = new ListAppender<>();

	@BeforeEach
	void beforeEach() {
		this.logger.setLevel(Level.INFO);
		this.appender.start();
		this.logger.addAppender(this.appender);
	}

	@AfterEach
	void afterEach() {
		this.logger.detachAppender(this.appender);
	}

	@Test
	void test_save_logs_one_line_per_request() {
		RawDataSaveService service = new RawDataSaveService();

		service.save(new Payload("zoom", "meeting.ended", "{\"event\":\"meeting.ended\"}"));
		service.save(new Payload("gohighlevel", "ContactCreate", "{\n  \"type\": \"ContactCreate\"\n}"));

		assertThat(this.appender.list).extracting(ILoggingEvent::getFormattedMessage).containsExactly(
			"zoom; meeting.ended; {\"event\":\"meeting.ended\"}",
			"gohighlevel; ContactCreate; {   \"type\": \"ContactCreate\" }"
		);
	}
}
