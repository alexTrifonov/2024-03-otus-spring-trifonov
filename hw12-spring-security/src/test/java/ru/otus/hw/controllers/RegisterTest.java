package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.SecurityConfig;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.services.UserService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
@DisplayName("Интеграционный тест страниц аутентификации и регистрации")
public class RegisterTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("должен рендерить страницу регистрации юзера с атрибутами модели и правильным названием view " +
            "и без аутентифицированного юзера")
    void shouldRenderBooksPageWithCorrectViewAndModelAttributes() throws Exception {
        UserDto expectedUser = new UserDto(null, null, null);
        mockMvc.perform(get("/register"))
                .andExpect(view().name("register"))
                .andExpect(model().attribute("user", expectedUser));
    }
}
