package br.car.registration.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.car.registration.api.v1.request.UserReq;
import br.car.registration.api.v1.response.UserRes;
import br.car.registration.domain.User;
import br.car.registration.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController(userService);
    }

    @Test
    void getAll_WhenCalled_ShouldReturnUsersList() {
        // Given
        UserRes userRes = new UserRes();
        List<UserRes> expectedUsers = List.of(userRes);
        
        when(userService.findAll()).thenReturn(expectedUsers);

        // When
        List<UserRes> result = userController.getAll();

        // Then
        assertEquals(expectedUsers, result);
        verify(userService).findAll();
    }

    @Test
    void getById_WhenValidId_ShouldReturnUser() {
        // Given
        UUID id = UUID.randomUUID();
        UserRes expectedUser = new UserRes();
        
        when(userService.findById(id)).thenReturn(expectedUser);

        // When
        UserRes result = userController.getById(id);

        // Then
        assertEquals(expectedUser, result);
        verify(userService).findById(id);
    }

    @Test
    void getByIdKeycloak_WhenValidIdKeycloak_ShouldReturnUser() {
        // Given
        String idKeycloak = "keycloak-123";
        UserRes expectedUser = new UserRes();
        
        when(userService.findByIdKeycloak(idKeycloak)).thenReturn(expectedUser);

        // When
        UserRes result = userController.getByIdKeycloak(idKeycloak);

        // Then
        assertEquals(expectedUser, result);
        verify(userService).findByIdKeycloak(idKeycloak);
    }

    @Test
    void createUser_WhenValidRequest_ShouldReturnCreatedUser() {
        // Given
        UserReq userReq = new UserReq();
        User expectedUser = new User();
        
        when(userService.createUser(userReq)).thenReturn(expectedUser);

        // When
        User result = userController.createUser(userReq);

        // Then
        assertEquals(expectedUser, result);
        verify(userService).createUser(userReq);
    }

    @Test
    void updateUserKeycloak_WhenValidRequest_ShouldReturnUpdatedUser() {
        // Given
        String idKeycloak = "keycloak-123";
        UserReq userReq = new UserReq();
        User expectedUser = new User();
        
        when(userService.updateUserKeycloak(idKeycloak, userReq)).thenReturn(expectedUser);

        // When
        User result = userController.updateUserKeycloak(idKeycloak, userReq);

        // Then
        assertEquals(expectedUser, result);
        verify(userService).updateUserKeycloak(idKeycloak, userReq);
    }

    @Test
    void updateUser_WhenValidRequest_ShouldReturnUpdatedUser() {
        // Given
        UUID id = UUID.randomUUID();
        UserReq userReq = new UserReq();
        User expectedUser = new User();
        
        when(userService.updateUser(id, userReq)).thenReturn(expectedUser);

        // When
        User result = userController.updateUser(id, userReq);

        // Then
        assertEquals(expectedUser, result);
        verify(userService).updateUser(id, userReq);
    }

    @Test
    void deleteUser_WhenValidId_ShouldCallService() {
        // Given
        UUID id = UUID.randomUUID();
        doNothing().when(userService).deleteUser(id);

        // When
        userController.deleteUser(id);

        // Then
        verify(userService).deleteUser(id);
    }

    @Test
    void constructor_WhenCalled_ShouldCreateController() {
        // When
        UserController controller = new UserController(userService);

        // Then
        assertNotNull(controller);
    }
}