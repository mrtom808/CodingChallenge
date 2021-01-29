package com.example.pushproxy.notification;

import com.example.pushproxy.model.Link;
import com.example.pushproxy.model.Note;
import com.example.pushproxy.model.Notification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class PushButtonNotificationSender implements NotificationSender {

    @Value("${pushButtonNotificationEndpoint:https://api.pushbullet.com/v2/pushes}")
    private String pushButtonNotificationEndpoint;

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Override
    public void sendNotification(Notification notification, String accessToken) {
        try {
            sendNotificationInternal(notification, accessToken);
        } catch (Exception e) {
            throw new NotificationException();
        }
    }

    public void sendNotificationInternal(Notification notification, String accessToken) throws IOException, InterruptedException {
        var requestBody = createPushBulletPushJson(notification);

        var request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(pushButtonNotificationEndpoint))
                .header("Access-Token", accessToken)
                .header("Content-Type", MediaType.APPLICATION_JSON.toString())
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() != HttpStatus.OK.value()) {
            throw new RuntimeException("");
        }
    }

    public String createPushBulletPushJson(Notification notification) throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        Map<String, String> pushBulletMap = new HashMap<>();
        pushBulletMap.put("type", "note");
        pushBulletMap.put("body", notification.getBody());

        if (notification instanceof Note) {
            pushBulletMap.put("title", ((Note) notification).getTitle());
        } else if (notification instanceof Link) {
            pushBulletMap.put("title", ((Link) notification).getTitle());
            pushBulletMap.put("url", ((Link) notification).getUrl());
        }

        String requestBody = objectMapper
                .writeValueAsString(pushBulletMap);
        return requestBody;
    }
}
