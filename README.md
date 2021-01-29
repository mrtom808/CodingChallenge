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

# Using
### Register a new user
```
curl --header 'Content-Type: application/json' --data-binary '{ "username": "user1", "accessToken": "o.xyz" }' --request POST  http://localhost:8080/v2/users
```

### List users
```
curl  http://localhost:8080/v2/users
```

### Send notification
```
curl --header 'Content-Type: application/json' --data-binary '{ "username": "user1", "type": "Note", "title": "Hello", "body": "world" }' --request POST  http://localhost:8080/v2/pushes
```