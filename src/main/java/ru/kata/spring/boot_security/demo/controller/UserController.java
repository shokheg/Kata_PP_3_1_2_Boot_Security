package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shokhalevich
 */

@Controller
@RequestMapping("/")
public class UserController {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/admin")
    public String findAll(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName()); // username == email
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("authUser", user);
        List<String> rolesList =
      user.getRoles().stream()
                .map(r -> r.getName().replace("ROLE_","")).collect(Collectors.toList());
        model.addAttribute("rolesList", rolesList);
        return "user-list";
    }

    @GetMapping("/user")
    public String showCurrentUser(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName()); // username == email
        List<User> users = new ArrayList<>();
        users.add(user);
        model.addAttribute("users", users);
        model.addAttribute("authUser", user);
        List<String> rolesList =
                user.getRoles().stream()
                        .map(r -> r.getName().replace("ROLE_","")).collect(Collectors.toList());
        model.addAttribute("rolesList", rolesList);
        return "user";
    }


}
