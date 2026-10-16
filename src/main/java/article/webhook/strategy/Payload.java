package article.webhook.strategy;

public record Payload(String platform, String event, String rawData) {

}
