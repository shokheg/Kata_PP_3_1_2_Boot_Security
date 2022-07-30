package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shokhalevich
 */

@RestController
@RequestMapping("/api")
public class UserRestController {
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserRestController(UserService userService, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    public ResponseEntity<?> findAll(Principal principal) {
        User user = userService.findByUsername(principal.getName()); // username == email
        List<User> users = userService.findAll();
        Map<String, Object> result = new HashMap<>();
        result.put("users", users);
        result.put("authUser", user);
        return new ResponseEntity<>(users, HttpStatus.OK);  //todo
    }

    @GetMapping("/user") //show AUTH USER
    public ResponseEntity<?> showCurrentUser(Principal principal) {
        User user = userService.findByUsername(principal.getName()); // username == email
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/users/{id}") //show user by ID
    public ResponseEntity<?> findUserById(@PathVariable("id") Long id) {
        User user = userService
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRolesList() {
        return new ResponseEntity<>(roleRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping(value = "/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        System.out.println(user + "++++++++++++++++++++++++++++++++++++++");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return new ResponseEntity<>("User deleted", HttpStatus.OK);
    }


    @PatchMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        System.out.println(user + "++++++++++++++++++++++++++++++++++++++");
        User oldUser = userService
                .findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + user.getId()));

        if (!user.getPassword().equals(oldUser.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userService.saveUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


}
