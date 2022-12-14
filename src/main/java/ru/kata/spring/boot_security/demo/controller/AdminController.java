package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RegistrationService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PasswordEncoder passwordEncoder;

    private final RegistrationService registrationService;

    private final UserService userService;

    @Autowired
    public AdminController(RegistrationService registrationService, PasswordEncoder passwordEncoder, UserService userService) {
        this.registrationService = registrationService;

        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @GetMapping
    public String getFormPrintAllUsers(Model model) {
        model.addAttribute("people", userService.getAllUsers());
        return "adminViews/admin";
    }

    @GetMapping("/{id}")
    public String getFormWithUserData(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", registrationService.findUserById(id));
        return "adminViews/showAdm";
    }

    @PostMapping("/{id}")
    public String deleteUserInDataBase(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String getFormForUpdateUser(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("user", userService.showUser(id));
        return "adminViews/edit";
    }

    @PostMapping(value = "update/{id}")
    public String updateUserInDataBase(@ModelAttribute("user") @Valid User user,
                                       @PathVariable("id") int id) {
        userService.updateUser(id, user);
        return "redirect:/admin";
    }

    @GetMapping("/users/new")
    public String getFormForNewUser(Model model) {
        model.addAttribute("user", new User());
        return "adminViews/new";
    }

    @PostMapping("/users/newUsers")
    public String addUserDataBase(@ModelAttribute("user") User user, @RequestParam(value = "my_roles[]") String[] roles) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.createUser(user, roles);
        return "redirect:/admin";
    }
}