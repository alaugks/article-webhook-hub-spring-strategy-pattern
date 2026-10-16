package article.webhook.strategy.gohighlevel;

import article.webhook.service.RawDataSaveService;
import article.webhook.strategy.Payload;
import article.webhook.strategy.SubStrategyFactory;
import article.webhook.strategy.SubStrategyInterface;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoHighLevelPlatformStrategyTest {

	@Mock
	private SubStrategyFactory subStrategyFactoryMock;

	@Mock
	private RawDataSaveService rawDataSaveServiceMock;

	@Mock
	private SubStrategyInterface subStrategyMock;

	private GoHighLevelPlatformStrategy strategy;

	@BeforeEach
	void beforeEach() {
		this.strategy = new GoHighLevelPlatformStrategy(new ObjectMapper(), this.subStrategyFactoryMock, this.rawDataSaveServiceMock);
	}

	@Test
	void test_support() {
		assertThat(this.strategy.supported("gohighlevel")).isTrue();
		assertThat(this.strategy.supported("zoom")).isFalse();
	}

	@Test
	void test_handle_reads_event_from_type_property() {
		when(this.subStrategyFactoryMock.getAllSubStrategies("gohighlevel", "InboundMessage"))
			.thenReturn(List.of(this.subStrategyMock));

		String payload = "{\"type\":\"InboundMessage\",\"messageType\":\"Email\"}";

		this.strategy.handle(payload, new HttpHeaders());

		verify(this.subStrategyMock, times(1)).handle(new Payload("gohighlevel", "InboundMessage", payload));
	}

	@Test
	void test_handle_stores_raw_data() {
		when(this.subStrategyFactoryMock.getAllSubStrategies("gohighlevel", "InboundMessage")).thenReturn(List.of());

		String payload = "{\"type\":\"InboundMessage\",\"messageType\":\"Email\"}";

		this.strategy.handle(payload, new HttpHeaders());

		verify(this.rawDataSaveServiceMock, times(1)).save(new Payload("gohighlevel", "InboundMessage", payload));
	}

	@Test
	void test_handle_invalid_json() {
		assertThatThrownBy(() -> this.strategy.handle("{", new HttpHeaders())).isInstanceOf(JacksonException.class);
	}

	@Test
	void test_getResponse() {
		assertThat(this.strategy.getResponse().getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
	}
}
