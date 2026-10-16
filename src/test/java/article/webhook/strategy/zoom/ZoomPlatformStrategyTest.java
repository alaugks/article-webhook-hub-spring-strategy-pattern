package article.webhook.strategy.zoom;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ZoomPlatformStrategyTest {

	@Mock
	private SubStrategyFactory subStrategyFactoryMock;

	@Mock
	private RawDataSaveService rawDataSaveServiceMock;

	@Mock
	private SubStrategyInterface subStrategy1;

	@Mock
	private SubStrategyInterface subStrategy2;

	private ZoomPlatformStrategy strategy;

	@BeforeEach
	void beforeEach() {
		this.strategy = new ZoomPlatformStrategy(new ObjectMapper(), this.subStrategyFactoryMock, this.rawDataSaveServiceMock);
	}

	@Test
	void test_support() {
		assertThat(this.strategy.supported("zoom")).isTrue();
		assertThat(this.strategy.supported("ZOOM")).isFalse();
		assertThat(this.strategy.supported("gohighlevel")).isFalse();
	}

	@Test
	void test_handle_calls_all_sub_strategies() {
		when(this.subStrategyFactoryMock.getAllSubStrategies("zoom", "meeting.ended"))
			.thenReturn(List.of(this.subStrategy1, this.subStrategy2));

		String payload = "{\"event\":\"meeting.ended\",\"payload\":{}}";

		this.strategy.handle(payload, new HttpHeaders());

		Payload expected = new Payload("zoom", "meeting.ended", payload);

		verify(this.subStrategy1, times(1)).handle(expected);
		verify(this.subStrategy2, times(1)).handle(expected);
	}

	@Test
	void test_handle_stores_raw_data() {
		when(this.subStrategyFactoryMock.getAllSubStrategies("zoom", "meeting.ended")).thenReturn(List.of());

		String payload = "{\"event\":\"meeting.ended\",\"payload\":{}}";

		this.strategy.handle(payload, new HttpHeaders());

		verify(this.rawDataSaveServiceMock, times(1)).save(new Payload("zoom", "meeting.ended", payload));
	}

	@Test
	void test_handle_without_matching_sub_strategy() {
		when(this.subStrategyFactoryMock.getAllSubStrategies("zoom", "unknown")).thenReturn(List.of());

		this.strategy.handle("{\"event\":\"unknown\"}", new HttpHeaders());

		verify(this.subStrategyFactoryMock).getAllSubStrategies("zoom", "unknown");
	}

	@Test
	void test_handle_invalid_json() {
		assertThatThrownBy(() -> this.strategy.handle("{", new HttpHeaders())).isInstanceOf(JacksonException.class);
		verify(this.rawDataSaveServiceMock, never()).save(any());
		verify(this.subStrategyFactoryMock, never()).getAllSubStrategies(any(), any());
	}

	@Test
	void test_getResponse() {
		assertThat(this.strategy.getResponse().getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
	}
}
