package article.webhook.strategy.zoom;

import article.webhook.strategy.EventNameInterface;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZoomRootDto implements EventNameInterface {

	@JsonProperty("event")
	private String event;

	@Override
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
}
