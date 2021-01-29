package com.example.pushproxy.api;

import com.example.pushproxy.model.Note;
import com.example.pushproxy.model.UserDetail;
import com.example.pushproxy.notification.PushButtonNotificationSender;
import com.example.pushproxy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

class PushesApiTest {
    private PushesApiController pushesApiController;
    private PushesApiImpl pushesApi;
    private PushButtonNotificationSender pushButtonNotificationSender;
    private UserRepository userRepository;


    @BeforeEach
    public void setupUserRepository() {
        userRepository = Mockito.mock(UserRepository.class);
        pushButtonNotificationSender = Mockito.mock(PushButtonNotificationSender.class);
        pushesApi = new PushesApiImpl();
        ReflectionTestUtils.setField(pushesApi, "userRepository", userRepository);
        ReflectionTestUtils.setField(pushesApi, "pushButtonNotificationSender", pushButtonNotificationSender);
        pushesApiController = new PushesApiController(pushesApi);
    }

    @Test
    public void testPushNotification() {
        var note = new Note();
        note.setTitle("Hello");
        note.setBody("Hello");
        note.setUsername("user1");

        var userDetail = getUserDetail();

        Mockito.when(userRepository.getUser(Mockito.matches("user1"))).thenReturn(userDetail);
        pushesApiController.sendNotification(note);

        Mockito.verify(userRepository).getUser(Mockito.matches("user1"));
        Mockito.verify(userRepository).incrementSendCountForUser(Mockito.matches("user1"));
        Mockito.verify(pushButtonNotificationSender).sendNotification(Mockito.same(note), Mockito.matches("secretXYZ"));
        Mockito.verifyNoMoreInteractions(userRepository);
        Mockito.verifyNoMoreInteractions(pushButtonNotificationSender);

    }

    private UserDetail getUserDetail() {
        UserDetail userDetail = new UserDetail();
        userDetail.setUsername("user1");
        userDetail.setCreationTime("now");
        userDetail.setNumOfNotificationsPushed(0);
        userDetail.setAccessToken("secretXYZ");
        return userDetail;
    }
}