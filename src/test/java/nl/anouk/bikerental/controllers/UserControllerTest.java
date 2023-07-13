package nl.anouk.bikerental.controllers;

import nl.anouk.bikerental.dtos.UserDto;
import nl.anouk.bikerental.repositories.CustomerRepository;
import nl.anouk.bikerental.repositories.UserRepository;
import nl.anouk.bikerental.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class UserControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private UserRepository userRepository;

    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userController = new UserController(userService, customerRepository, userRepository);
    }

    @Test
    @WithMockUser(username = "testuser", roles = "ADMIN")
    public void testGetUsers_ShouldReturnListOfUserDto() {
        // Arrange
        List<UserDto> expectedDtos = createUserDtoList();
        when(userService.getUsers()).thenReturn(expectedDtos);

        // Act
        ResponseEntity<List<UserDto>> response = userController.getUsers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDtos, response.getBody());
        verify(userService, times(1)).getUsers();
    }

    @Test
    @WithMockUser(username = "testuser", roles = "ADMIN")
    public void testGetUser_ShouldReturnUserDto() {
        // Arrange
        String username = "testuser";
        UserDto expectedDto = createUserDto(username);
        when(userService.getUser(username)).thenReturn(expectedDto);

        // Act
        ResponseEntity<UserDto> response = userController.getUser(username);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDto, response.getBody());
        verify(userService, times(1)).getUser(username);
    }

    @Test
    @WithMockUser(username = "testuser", roles = "ADMIN")
    public void testUpdateUser_ShouldReturnNoContent() {
        // Arrange
        String username = "testuser";
        UserDto dto = createUserDto(username);

        // Act
        ResponseEntity<UserDto> response = userController.updateUser(username, dto);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Assertions.assertNull(response.getBody());
        verify(userService, times(1)).updateUser(username, dto);
    }

    @Test
    @WithMockUser(username = "testuser", roles = "ADMIN")
    public void testDeleteUser_ShouldReturnNoContent() {
        // Arrange
        String username = "testuser";

        // Act
        ResponseEntity<Object> response = userController.deleteUser(username);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Assertions.assertNull(response.getBody());
        verify(userService, times(1)).deleteUser(username);
    }


    @Test
    @WithMockUser(username = "testuser", roles = "ADMIN")
    public void testAddUserAuthority_ShouldReturnNoContent() {
        // Arrange
        String username = "testuser";
        Map<String, Object> fields = new HashMap<>();
        fields.put("authority", "ROLE_ADMIN");

        doNothing().when(userService).addAuthority(username, "ROLE_ADMIN");

        // Act
        ResponseEntity<Object> response = userController.addUserAuthority(username, fields);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService).addAuthority(username, "ROLE_ADMIN");

    }
        @Test
        @WithMockUser(username = "testuser", roles = "ADMIN")
        public void testDeleteUserAuthority_ShouldReturnNoContent () {
            // Arrange
            String username = "testuser";
            String authority = "ROLE_ADMIN";

            // Act
            ResponseEntity<Object> response = userController.deleteUserAuthority(username, authority);

            // Assert
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            Assertions.assertNull(response.getBody());
            verify(userService, times(1)).removeAuthority(username, authority);
        }

        private UserDto createUserDto (String username){
            UserDto dto = new UserDto();
            dto.setUsername(username);

            return dto;
        }

        private List<UserDto> createUserDtoList () {
            List<UserDto> dtos = new ArrayList<>();
            dtos.add(createUserDto("user1"));
            dtos.add(createUserDto("user2"));
            return dtos;
        }

        private List<String> createAuthorityList () {
            List<String> authorities = new ArrayList<>();
            authorities.add("ROLE_ADMIN");
            authorities.add("ROLE_USER");
            return authorities;
        }
    }
