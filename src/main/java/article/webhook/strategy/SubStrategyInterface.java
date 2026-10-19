package article.webhook.strategy;

public interface SubStrategyInterface {

	boolean supported(String platform, String event);

	void handle(Payload payload);
}
