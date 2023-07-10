

package nl.anouk.bikerental.controllers;

import jakarta.validation.Valid;
import nl.anouk.bikerental.dtos.UserDto;
import nl.anouk.bikerental.exceptions.BadRequestException;
import nl.anouk.bikerental.repositories.CustomerRepository;
import nl.anouk.bikerental.repositories.UserRepository;
import nl.anouk.bikerental.services.UserService;
import nl.anouk.bikerental.utils.Utilities;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public UserController(UserService userService, CustomerRepository customerRepository, UserRepository userRepository) {
        this.userService = userService;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }


    @GetMapping(value = "")
    public ResponseEntity<List<UserDto>> getUsers() {

        List<UserDto> userDtos = userService.getUsers();

        return ResponseEntity.ok().body(userDtos);
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable("username") String username) {

        UserDto optionalUser = userService.getUser(username);


        return ResponseEntity.ok().body(optionalUser);

    }

    @PutMapping(value = "/{username}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("username") String username, @RequestBody UserDto dto) {

        userService.updateUser(username, dto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{username}")
    public ResponseEntity<Object> deleteUser(@PathVariable("username") String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{username}/authorities")
    public ResponseEntity<Object> getUserAuthorities(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(userService.getAuthorities(username));
    }

   @PostMapping(value = "create_user")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto dto, BindingResult br) {
       if (br.hasErrors()) {
           String errorString = Utilities.getErrorString(br);
           return new ResponseEntity<>(errorString, HttpStatus.BAD_REQUEST);
       }
        if (userRepository.existsByEmail(dto.getEmail())) {
           throw new IllegalArgumentException("Email already exists");
       }

        String newUsername = userService.createUser(dto);
        userService.addAuthority(newUsername, "ROLE_USER");

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}")
                .buildAndExpand(newUsername).toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping(value = "/{username}/authorities")
    public ResponseEntity<Object> addUserAuthority(@PathVariable("username") String username, @RequestBody Map<String, Object> fields) {
        try {
            String authorityName = (String) fields.get("authority");
            userService.addAuthority(username, authorityName);
            return ResponseEntity.noContent().build();
        }
        catch (Exception ex) {
            throw new BadRequestException();
        }
    }

    @DeleteMapping(value = "/{username}/authorities/{authority}")
    public ResponseEntity<Object> deleteUserAuthority(@PathVariable("username") String username, @PathVariable("authority") String authority) {
        userService.removeAuthority(username, authority);
        return ResponseEntity.noContent().build();
    }

}
