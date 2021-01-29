package com.example.pushproxy.api;

import com.example.pushproxy.model.User;
import com.example.pushproxy.model.UserDetail;
import com.example.pushproxy.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;


@RunWith(SpringRunner.class)
class UsersApiControllerTest {

    private UsersApiController usersApiController;
    private UserApiImpl userApi;
    private UserRepository userRepository;


    @BeforeEach
    public void setupUserRepository() {
        userRepository = Mockito.mock(UserRepository.class);
        userApi = new UserApiImpl();
        ReflectionTestUtils.setField(userApi, "userRepository", userRepository);
        usersApiController = new UsersApiController(userApi);
    }

    @Test
    public void testCreateUser() {
        var userDetail = getUserDetail();
        Mockito.when(userRepository.createUser(Mockito.any(User.class))).thenReturn(userDetail);

        var user = getUser();
        var returnedUser = usersApiController.createUser(user);
        Assert.assertSame(userDetail, returnedUser.getBody());
        Assert.assertSame(HttpStatus.CREATED, returnedUser.getStatusCode());

        Mockito.verify(userRepository).createUser(user);
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testGetUsers() {
        var userDetail = getUserDetail();
        Mockito.when(userRepository.getUsers()).thenReturn(List.of(userDetail));

        var user = getUser();
        var returnedUser = usersApiController.getAllUsers();
        Assert.assertEquals(1, returnedUser.getBody().size());
        Assert.assertSame(userDetail, returnedUser.getBody().get(0));
        Assert.assertSame(HttpStatus.OK, returnedUser.getStatusCode());

        Mockito.verify(userRepository).getUsers();
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    private User getUser() {
        User user = new User();
        user.setUsername("user1");
        user.setAccessToken("secretXYZ");
        return user;
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