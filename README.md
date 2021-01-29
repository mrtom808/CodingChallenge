# Demo REST proxy for sending notifications to the Pushbullet service

### Frameworks & tools used
- Spring Boot
- Openapi
- gradle
- Java 11 HttpClient

## Notes
- Uses openapi to generate code and documentation of the REST API. Openapi was surprisingly buggy and I had expected it to be more mature than it is.
- In a complete implementation, tests for the REST client and user repository would be created
- For thread safety, I took the short route and used a synchronized method to keep count of the number of notifications sent.
- The push service implements sending links and notes but not files


# Running

```
./gradlew bootRun
```
