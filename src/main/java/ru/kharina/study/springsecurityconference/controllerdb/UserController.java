package ru.kharina.study.springsecurityconference.controllerdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kharina.study.springsecurityconference.model.User;
import ru.kharina.study.springsecurityconference.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('permission:admin')")
    public List<User> getAllUser() {
        return userService.getAllUsers();
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('permission:admin')")
    public User createAuditorium(@RequestBody User newUser) {
        return userService.saveUser(newUser);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:admin')")
    public User getUserDtoById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:admin')")
    public User updateUser(@RequestBody User newUser, @PathVariable int id) {
        return userService.updateUser(newUser, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:admin')")
    public void deleteAuditorium(@PathVariable int id) {
        userService.deleteUserById(id);
    }
}

