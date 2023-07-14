package nl.anouk.bikerental.services;

import nl.anouk.bikerental.dtos.UserDto;
import nl.anouk.bikerental.exceptions.RecordNotFoundException;
import nl.anouk.bikerental.models.Authority;
import nl.anouk.bikerental.models.User;
import nl.anouk.bikerental.repositories.CustomerRepository;
import nl.anouk.bikerental.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
@InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(userRepository, customerRepository);
        userService.passwordEncoder = passwordEncoder;
    }

    @Test
    public void testGetUsers() {
        // Arrange
        List<User> userList = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");
        userList.add(user1);
        userList.add(user2);
        Mockito.when(userRepository.findAll()).thenReturn(userList);

        // Act
        List<UserDto> result = userService.getUsers();

        // Assert
        Assertions.assertEquals(userList.size(), result.size());
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void testGetUser_Success() {
        // Arrange
        String username = "user1";
        User user = new User();
        user.setUsername(username);
        Mockito.when(userRepository.findById(username)).thenReturn(Optional.of(user));

        // Act
        UserDto result = userService.getUser(username);

        // Assert
        Assertions.assertEquals(username, result.getUsername());
        Mockito.verify(userRepository, Mockito.times(1)).findById(username);
    }

    @Test
    public void testGetUser_RecordNotFoundException() {
        // Arrange
        String username = "user1";
        Mockito.when(userRepository.findById(username)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(RecordNotFoundException.class, () -> {
            userService.getUser(username);
        });
        Mockito.verify(userRepository, Mockito.times(1)).findById(username);
    }

    @Test
    public void testUserExists_True() {
        // Arrange
        String username = "user1";
        Mockito.when(userRepository.existsById(username)).thenReturn(true);

        // Act
        boolean result = userService.userExists(username);

        // Assert
        Assertions.assertTrue(result);
        Mockito.verify(userRepository, Mockito.times(1)).existsById(username);
    }

    @Test
    public void testUserExists_False() {
        // Arrange
        String username = "user1";
        Mockito.when(userRepository.existsById(username)).thenReturn(false);

        // Act
        boolean result = userService.userExists(username);

        // Assert
        Assertions.assertFalse(result);
        Mockito.verify(userRepository, Mockito.times(1)).existsById(username);
    }


    @Test
    public void testDeleteUser() {
        // Arrange
        String username = "user1";

        // Act
        userService.deleteUser(username);

        // Assert
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(username);
    }

    @Test
    public void testUpdateUser_Success() {
        // Arrange
        String username = "user1";
        UserDto newUser = new UserDto();
        newUser.setPassword("newPassword");

        User user = new User();
        user.setUsername(username);
        Mockito.when(userRepository.existsById(username)).thenReturn(true);
        Mockito.when(userRepository.findById(username)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        // Act
        userService.updateUser(username, newUser);

        // Assert
        Mockito.verify(userRepository, Mockito.times(1)).existsById(username);
        Mockito.verify(userRepository, Mockito.times(1)).findById(username);
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    public void testUpdateUser_RecordNotFoundException() {
        // Arrange
        String username = "user1";
        UserDto newUser = new UserDto();
        newUser.setPassword("newPassword");
        Mockito.when(userRepository.existsById(username)).thenReturn(false);

        // Act & Assert
        Assertions.assertThrows(RecordNotFoundException.class, () -> {
            userService.updateUser(username, newUser);
        });
        Mockito.verify(userRepository, Mockito.times(1)).existsById(username);
        Mockito.verify(userRepository, Mockito.never()).findById(Mockito.anyString());
        Mockito.verify(passwordEncoder, Mockito.never()).encode(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
    }

    @Test
    public void testGetAuthorities_Success() {
        // Arrange
        String username = "user1";
        User user = new User();
        user.setUsername(username);
        Authority authority = new Authority(username, "ROLE_ADMIN");
        user.addAuthority(authority);
        Mockito.when(userRepository.existsById(username)).thenReturn(true);
        Mockito.when(userRepository.findById(username)).thenReturn(Optional.of(user));

        // Act
        Set<Authority> result = userService.getAuthorities(username);

        // Assert
        Assertions.assertEquals(1, result.size());
        Assertions.assertTrue(result.contains(authority));
        Mockito.verify(userRepository, Mockito.times(1)).existsById(username);
        Mockito.verify(userRepository, Mockito.times(1)).findById(username);
    }

    @Test
    public void testGetAuthorities_RecordNotFoundException() {
        // Arrange
        String username = "user1";
        Mockito.when(userRepository.existsById(username)).thenReturn(false);

        // Act & Assert
        Assertions.assertThrows(RecordNotFoundException.class, () -> {
            userService.getAuthorities(username);
        });
        Mockito.verify(userRepository, Mockito.times(1)).existsById(username);
        Mockito.verify(userRepository, Mockito.never()).findById(Mockito.anyString());
    }

    @Test
    public void testAddAuthority_Success() {
        // Arrange
        String username = "user1";
        String authority = "ROLE_ADMIN";
        User user = new User();
        user.setUsername(username);
        Mockito.when(userRepository.existsById(username)).thenReturn(true);
        Mockito.when(userRepository.findById(username)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        // Act
        userService.addAuthority(username, authority);

        // Assert
        Assertions.assertEquals(1, user.getAuthorities().size());
        Mockito.verify(userRepository, Mockito.times(1)).existsById(username);
        Mockito.verify(userRepository, Mockito.times(1)).findById(username);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    public void testAddAuthority_RecordNotFoundException() {
        // Arrange
        String username = "user1";
        String authority = "ROLE_ADMIN";
        Mockito.when(userRepository.existsById(username)).thenReturn(false);

        // Act & Assert
        Assertions.assertThrows(RecordNotFoundException.class, () -> {
            userService.addAuthority(username, authority);
        });
        Mockito.verify(userRepository, Mockito.times(1)).existsById(username);
        Mockito.verify(userRepository, Mockito.never()).findById(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
    }

    @Test
    public void testRemoveAuthority_Success() {
        // Arrange
        String username = "user1";
        String authorityToRemove = "ROLE_ADMIN";
        User user = new User();
        user.setUsername(username);
        Authority authority = new Authority(username, authorityToRemove);
        user.addAuthority(authority);
        Mockito.when(userRepository.existsById(username)).thenReturn(true);
        Mockito.when(userRepository.findById(username)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        // Act
        userService.removeAuthority(username, authorityToRemove);

        // Assert
        Assertions.assertEquals(0, user.getAuthorities().size());
        Mockito.verify(userRepository, Mockito.times(1)).existsById(username);
        Mockito.verify(userRepository, Mockito.times(1)).findById(username);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    public void testRemoveAuthority_RecordNotFoundException() {
        // Arrange
        String username = "user1";
        String authorityToRemove = "ROLE_ADMIN";
        Mockito.when(userRepository.existsById(username)).thenReturn(false);

        // Act & Assert
        Assertions.assertThrows(RecordNotFoundException.class, () -> {
            userService.removeAuthority(username, authorityToRemove);
        });
        Mockito.verify(userRepository, Mockito.times(1)).existsById(username);
        Mockito.verify(userRepository, Mockito.never()).findById(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
    }
}
