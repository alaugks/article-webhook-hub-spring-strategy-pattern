package article.webhook.strategy.zoom;

import article.webhook.service.RawDataSaveService;
import article.webhook.strategy.Payload;
import article.webhook.strategy.PlatformStrategyAbstract;
import article.webhook.strategy.SubStrategyFactory;
import article.webhook.strategy.SubStrategyInterface;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class ZoomPlatformStrategy extends PlatformStrategyAbstract {

	public static final String NAME = "zoom";

	private final ObjectMapper objectMapper;
	private final SubStrategyFactory subStrategyFactory;
	private final RawDataSaveService rawDataSaveService;

	public ZoomPlatformStrategy(
		ObjectMapper objectMapper,
		SubStrategyFactory subStrategyFactory,
		RawDataSaveService rawDataSaveService
	) {
		super(NAME);
		this.objectMapper = objectMapper;
		this.subStrategyFactory = subStrategyFactory;
		this.rawDataSaveService = rawDataSaveService;
	}

	@Override
	public void handle(String rawPayload, HttpHeaders headers) {
		// Read the event name from the payload ("event" property)
		ZoomRootDto dto = this.objectMapper.readValue(rawPayload, ZoomRootDto.class);

		Payload payload = new Payload(NAME, dto.getEvent(), rawPayload);

		this.rawDataSaveService.save(payload);

		// Call all sub strategies that support strategy + event
		for (SubStrategyInterface subStrategy : this.subStrategyFactory.getAllSubStrategies(NAME, dto.getEvent())) {
			subStrategy.handle(payload);
		}
	}
}
