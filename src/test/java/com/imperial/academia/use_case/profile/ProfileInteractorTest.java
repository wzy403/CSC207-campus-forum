package com.imperial.academia.use_case.profile;

import com.imperial.academia.app.ServiceFactory;
import com.imperial.academia.app.UsecaseFactory;
import com.imperial.academia.entity.user.User;
import com.imperial.academia.service.UserService;
import com.imperial.academia.session.SessionManager;
import com.imperial.academia.use_case.changeview.ChangeViewInputBoundary;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static junit.framework.Assert.assertFalse;

public class ProfileInteractorTest {
    private ProfileOutputBoundry mockPresenter;
    private UserService mockUserService;
    private ChangeViewInputBoundary mockChangeViewInteractor;
    private ProfileInteractor profileInteractor;
    private User testUser;
    private User testUser1;
    private ProfileOutputData outputUser;
    private String outputError;

    @Before
    public void init(){

        testUser = new User(
                877874918,
                "testName",
                "password",
                "test@mail.com",
                "testPerson",
                "testUrl",
                new Timestamp(0),
                new Timestamp(0)
        );
        testUser1 = new User(
                87787491,
                "testName",
                "password",
                "test@mail.com",
                "testPerson",
                "testUrl",
                new Timestamp(0),
                new Timestamp(0)
        );
        mockPresenter = new ProfileOutputBoundry() {
            @Override
            public void present(ProfileOutputData user) {
                outputUser = user;
            }

            @Override
            public void presentError(String error) {
                outputError = error;
            }

        };
        mockUserService = new UserService() {
            @Override
            public void insert(User user) throws SQLException {
            }

            @Override
            public boolean existsByUsername(String username) throws SQLException {
                return false;
            }

            @Override
            public boolean existsByEmail(String email) throws SQLException {
                return false;
            }

            @Override
            public User getByUsername(String username) throws SQLException {
                return null;
            }

            @Override
            public User get(int id) throws SQLException {
                if (id == 877874918){
                    return testUser;
                }else if (id == 500){
                    throw new SQLException();
                }else{
                    return null;
                }
            }

            @Override
            public List<User> getAll() throws SQLException {
                return List.of();
            }

            @Override
            public void update(User user) throws SQLException {

            }

            @Override
            public void delete(int id) throws SQLException {

            }
        };

        mockChangeViewInteractor = new ChangeViewInputBoundary() {
            @Override
            public void changeView(String viewName) {
                Assert.assertEquals("profile", viewName);

            }
        };

        profileInteractor = new ProfileInteractor(mockPresenter, mockUserService,mockChangeViewInteractor);
    }

    @Test
    public void testExecuteFindUserIsMe() {
        try (MockedStatic<SessionManager> mockedStatic = Mockito.mockStatic(SessionManager.class)){
            mockedStatic.when(SessionManager::getCurrentUser).thenReturn(testUser);
            profileInteractor.execute(new ProfileInputData(877874918));
            Assert.assertEquals(877874918, outputUser.getId());
            Assert.assertEquals("testName", outputUser.getUsername());
            Assert.assertEquals("test@mail.com", outputUser.getEmail());
            Assert.assertEquals("testPerson", outputUser.getRole());
            Assert.assertEquals("testUrl", outputUser.getAvatarUrl());
            Assert.assertEquals(new Timestamp(0), outputUser.getRegistrationDate());
            Assert.assertTrue(outputUser.isMe());

        }
    }

    @Test
    public void testExecuteFindUserNotMe() {
        try (MockedStatic<SessionManager> mockedStatic = Mockito.mockStatic(SessionManager.class)){
            mockedStatic.when(SessionManager::getCurrentUser).thenReturn(testUser1);
            profileInteractor.execute(new ProfileInputData(877874918));
            Assert.assertEquals(877874918, outputUser.getId());
            Assert.assertEquals("testName", outputUser.getUsername());
            Assert.assertEquals("test@mail.com", outputUser.getEmail());
            Assert.assertEquals("testPerson", outputUser.getRole());
            Assert.assertEquals("testUrl", outputUser.getAvatarUrl());
            Assert.assertEquals(new Timestamp(0), outputUser.getRegistrationDate());
            Assert.assertFalse(outputUser.isMe());

        }
    }

    @Test
    public void testExecuteUserNotFind(){
        profileInteractor.execute(new ProfileInputData(123));
        Assert.assertEquals("User not found", outputError);
    }

    @Test
    public void testExecuteSQLError(){
        profileInteractor.execute(new ProfileInputData(500));
        Assert.assertEquals((new SQLException()).getMessage(), outputError);
    }

    @Test
    public void testConstructor() {
        try (MockedStatic<ServiceFactory> mockedStatic = Mockito.mockStatic(ServiceFactory.class)){
            try(MockedStatic<UsecaseFactory> mockedStatic1 = Mockito.mockStatic(UsecaseFactory.class)){
                mockedStatic.when(ServiceFactory::getUserService).thenReturn(mockUserService);
                mockedStatic1.when(UsecaseFactory::getChangeViewInteractor).thenReturn(mockChangeViewInteractor);
                ProfileInteractor profileInteractor1 = new ProfileInteractor(mockPresenter);


                Field userServiceField = ProfileInteractor.class.getDeclaredField("userService");
                userServiceField.setAccessible(true);
                Assert.assertEquals(userServiceField.get(profileInteractor), userServiceField.get(profileInteractor1));

                Field changeViewInteractorField = ProfileInteractor.class.getDeclaredField("changeViewInteractor");
                changeViewInteractorField.setAccessible(true);
                Assert.assertEquals(changeViewInteractorField.get(profileInteractor), changeViewInteractorField.get(profileInteractor1));

                Field profilePresenterField = ProfileInteractor.class.getDeclaredField("profilePresenter");
                profilePresenterField.setAccessible(true);
                Assert.assertEquals(profilePresenterField.get(profileInteractor), profilePresenterField.get(profileInteractor1));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }


        }
    }


}