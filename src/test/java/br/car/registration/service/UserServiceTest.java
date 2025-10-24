package br.car.registration.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.car.registration.api.v1.request.UserReq;
import br.car.registration.api.v1.response.UserRes;
import br.car.registration.domain.User;
import br.car.registration.mappers.UserMapper;
import br.car.registration.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserReq userReq;
    private UserRes userRes;
    private UUID userId;
    private String idKeycloak;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        idKeycloak = "keycloak-123";
        
        user = new User();
        user.setId(userId);
        user.setIdKeycloak(idKeycloak);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        
        userReq = new UserReq();
        userReq.setIdKeycloak(idKeycloak);
        userReq.setFirstName("John");
        userReq.setLastName("Doe");
        userReq.setEmail("john@example.com");
        
        userRes = new UserRes();
        userRes.setId(userId);
        userRes.setFirstName("John");
        userRes.setLastName("Doe");
    }

    @Test
    void findById_WhenUserExists_ShouldReturnUserRes() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userRes);

        // When
        UserRes result = userService.findById(userId);

        // Then
        assertNotNull(result);
        assertEquals(userRes.getId(), result.getId());
        verify(userRepository).findById(userId);
        verify(userMapper).toDTO(user);
    }

    @Test
    void findById_WhenUserNotExists_ShouldThrowException() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> userService.findById(userId));
        verify(userRepository).findById(userId);
    }

    @Test
    void createUser_WhenUserNotExists_ShouldCreateNewUser() {
        // Given
        when(userRepository.findByIdKeycloak(idKeycloak)).thenReturn(Optional.empty());
        when(userMapper.toEntity(userReq)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        // When
        User result = userService.createUser(userReq);

        // Then
        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository).findByIdKeycloak(idKeycloak);
        verify(userMapper).toEntity(userReq);
        verify(userRepository).save(user);
    }

    @Test
    void deleteUser_ShouldCallRepository() {
        // When
        userService.deleteUser(userId);

        // Then
        verify(userRepository).deleteById(userId);
    }

    @Test
    void findAll_ShouldReturnListOfUserRes() {
        // Given
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        when(userMapper.toDTO(user)).thenReturn(userRes);

        // When
        List<UserRes> result = userService.findAll();

        // Then
        assertEquals(1, result.size());
        assertEquals(userRes, result.get(0));
        verify(userRepository).findAll();
        verify(userMapper).toDTO(user);
    }

    @Test
    void findByIdKeycloak_WhenUserExists_ShouldReturnUserRes() {
        // Given
        when(userRepository.findByIdKeycloak(idKeycloak)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userRes);

        // When
        UserRes result = userService.findByIdKeycloak(idKeycloak);

        // Then
        assertEquals(userRes, result);
        verify(userRepository).findByIdKeycloak(idKeycloak);
        verify(userMapper).toDTO(user);
    }

    @Test
    void findByIdKeycloak_WhenUserNotExists_ShouldThrowException() {
        // Given
        when(userRepository.findByIdKeycloak(idKeycloak)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> userService.findByIdKeycloak(idKeycloak));
        verify(userRepository).findByIdKeycloak(idKeycloak);
    }

    @Test
    void createUser_WhenUserExists_ShouldUpdateExisting() {
        // Given
        when(userRepository.findByIdKeycloak(idKeycloak)).thenReturn(Optional.of(user));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        // When
        User result = userService.createUser(userReq);

        // Then
        assertEquals(user, result);
        verify(userRepository).findByIdKeycloak(idKeycloak);
        verify(userRepository).save(user);
    }

    @Test
    void updateUserKeycloak_WhenUserExists_ShouldUpdateUser() {
        // Given
        when(userRepository.findByIdKeycloak(idKeycloak)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        // When
        User result = userService.updateUserKeycloak(idKeycloak, userReq);

        // Then
        assertEquals(user, result);
        assertEquals(userReq.getFirstName(), user.getFirstName());
        assertEquals(userReq.getLastName(), user.getLastName());
        assertEquals(userReq.getEmail(), user.getEmail());
        verify(userRepository).findByIdKeycloak(idKeycloak);
        verify(userRepository).save(user);
    }

    @Test
    void updateUserKeycloak_WhenUserNotExists_ShouldThrowException() {
        // Given
        when(userRepository.findByIdKeycloak(idKeycloak)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> userService.updateUserKeycloak(idKeycloak, userReq));
        verify(userRepository).findByIdKeycloak(idKeycloak);
    }

    @Test
    void updateUser_WhenUserExists_ShouldUpdateUser() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        // When
        User result = userService.updateUser(userId, userReq);

        // Then
        assertEquals(user, result);
        assertEquals(userReq.getFirstName(), user.getFirstName());
        assertEquals(userReq.getLastName(), user.getLastName());
        assertEquals(userReq.getEmail(), user.getEmail());
        verify(userRepository).findById(userId);
        verify(userRepository).save(user);
    }

    @Test
    void updateUser_WhenUserNotExists_ShouldThrowException() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(userId, userReq));
        verify(userRepository).findById(userId);
    }
}