package article.webhook.controller;

import article.webhook.strategy.PlatformStrategyFactory;
import article.webhook.strategy.PlatformStrategyInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.core.exc.StreamReadException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WebhookPlatformStrategyController.class)
class WebhookPlatformStrategyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PlatformStrategyFactory platformStrategyFactoryMock;

	@MockitoBean
	private PlatformStrategyInterface platformStrategyMock;


	@BeforeEach
	void beforeEach() {
		when(this.platformStrategyFactoryMock.getPlatformStrategy("mock")).thenReturn(this.platformStrategyMock);
		when(this.platformStrategyMock.getResponse()).thenReturn(ResponseEntity.status(HttpStatus.OK).build());
	}

	@Test
	void test_strategy() throws Exception {
		mockMvc.perform(post("/mock")
			.content("{\"property\":\"value\"}")
			.contentType(MediaType.APPLICATION_JSON_VALUE)
		).andExpect(status().isOk());

		verify(this.platformStrategyMock).handle(eq("{\"property\":\"value\"}"), any(HttpHeaders.class));
	}

	@Test
	void test_strategy_name_with_uppercase_characters() throws Exception {
		mockMvc.perform(post("/MoCk")
			.content("{}")
			.contentType(MediaType.APPLICATION_JSON_VALUE)
		).andExpect(status().isNotFound())
			.andExpect(content().string(""));
	}

	@Test
	void test_strategy_name_with_invalid_characters() throws Exception {
		mockMvc.perform(post("/mock1")
			.content("{}")
			.contentType(MediaType.APPLICATION_JSON_VALUE)
		).andExpect(status().isNotFound());
	}

	@Test
	void test_strategy_name_with_16_characters() throws Exception {
		mockMvc.perform(post("/abcdefghijklmnop")
			.content("{}")
			.contentType(MediaType.APPLICATION_JSON_VALUE)
		).andExpect(status().isNotFound())
			.andExpect(content().json("{\"error\":\"PlatformStrategy [abcdefghijklmnop] not found\"}"));
	}

	@Test
	void test_strategy_name_with_17_characters() throws Exception {
		mockMvc.perform(post("/abcdefghijklmnopq")
			.content("{}")
			.contentType(MediaType.APPLICATION_JSON_VALUE)
		).andExpect(status().isNotFound())
			.andExpect(content().string(""));
	}

	@Test
	void test_strategy_not_found() throws Exception {
		mockMvc.perform(post("/notexists")
			.content("{}")
			.contentType(MediaType.APPLICATION_JSON_VALUE)
		).andExpect(status().isNotFound())
			.andExpect(content().json("{\"error\":\"PlatformStrategy [notexists] not found\"}"));
	}

	@Test
	void test_strategy_handle_json_error() throws Exception {
		doThrow(new StreamReadException("failed")).when(this.platformStrategyMock).handle(any(), any());

		mockMvc.perform(post("/mock")
			.content("{")
			.contentType(MediaType.APPLICATION_JSON_VALUE)
		).andExpect(status().isBadRequest())
			.andExpect(content().json("{\"error\":\"PlatformStrategy [mock] with JSON error\"}"));
	}

	@Test
	void test_strategy_handle_failed() throws Exception {
		doThrow(new RuntimeException("failed")).when(this.platformStrategyMock).handle(any(), any());

		mockMvc.perform(post("/mock")
			.content("{}")
			.contentType(MediaType.APPLICATION_JSON_VALUE)
		).andExpect(status().isInternalServerError())
			.andExpect(content().json("{\"error\":\"PlatformStrategy [mock] handle failed\"}"));
	}
}
