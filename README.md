# Webhook Hub with Spring Boot: Strategy Pattern, Sub-Strategies and Collection Injection

https://www.alaugks.dev/article/webhook-hub-with-spring-boot-strategy-pattern-sub-strategies-and-collection-injection.html

## Run locally with Docker

```bash
docker compose up --build
```

The hub listens on `http://localhost:8081`. Send a webhook:

```bash
curl -i -X POST http://localhost:8081/zoom \
  -H "Content-Type: application/json" \
  -d '{"event": "meeting.ended", "payload": {"object": {"id": "123456789"}}}'
```

`RawDataSaveService` and the sub-strategies only log at `INFO` level to illustrate the flow (no real persistence, no real follow-up action):

```bash
docker compose logs -f webhook-hub
```

For a `meeting.ended` event the platform strategy logs the raw data (`platformName; event; payload`, line breaks in the payload are replaced by spaces) and both `ZoomMeetingEndedSubStrategy` and `FoundrySubStrategy` log. For `meeting.started` only `FoundrySubStrategy` logs:

```
INFO  ... RawDataSaveService - zoom; meeting.ended; {"event": "meeting.ended", "payload": {"object": {"id": "123456789"}}}
INFO  ... FoundrySubStrategy - FoundrySubStrategy handled zoom / meeting.ended
INFO  ... ZoomMeetingEndedSubStrategy - ZoomMeetingEndedSubStrategy handled zoom / meeting.ended
```
