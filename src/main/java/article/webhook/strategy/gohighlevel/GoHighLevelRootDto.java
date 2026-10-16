package article.webhook.strategy.gohighlevel;

import article.webhook.strategy.EventNameInterface;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoHighLevelRootDto implements EventNameInterface {

	// GoHighLevel sends the event name in "type"
	@JsonProperty("type")
	private String event;

	@Override
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
}
