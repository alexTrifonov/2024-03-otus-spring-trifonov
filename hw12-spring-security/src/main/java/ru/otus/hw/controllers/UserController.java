package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.models.User;
import ru.otus.hw.services.UserService;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String addUser(Model model) {
        model.addAttribute("user", new UserDto(null, null, null));
        return "register";
    }

    @PostMapping("/register")
    public String addUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> log.error(error.getDefaultMessage()));
            return "register";
        }
        User user = new User();
        user.setLogin(userDto.login());
        user.setPassword(passwordEncoder.encode(userDto.password()));
        userService.save(user);
        return "redirect:/";
    }
}
