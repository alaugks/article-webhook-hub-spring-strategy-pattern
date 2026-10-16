package article.webhook.strategy;

public interface SubStrategyInterface {

	boolean supported(String platformName, String eventName);

	void handle(Payload payload);
}
