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

import java.util.List;

/**
 * @author shokhalevich
 */

@Controller
@RequestMapping("/admin")
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

    @GetMapping("")
    public String findAll(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "user-list";
    }

    @GetMapping(value = "/user-create")
    public String createUserForm(User user, Model model) {
        return "user-create";
    }

    @ModelAttribute("rolesList")
    public List<Role> getRolesList() {
        return roleRepository.findAll();
    }

    @PostMapping(value = "/user-create")
    public String createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping(value = "user-delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

    @GetMapping(value = "user-update/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model) {

        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        model.addAttribute("user", user);
        return "/user-update";
    }

    @PatchMapping("/user-update")
    public String updateUser(User user) {
        User oldUser = userService
                .findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + user.getId()));

        if (!user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(oldUser.getPassword());
        }

        userService.saveUser(user);
        return "redirect:/admin";
    }


}
