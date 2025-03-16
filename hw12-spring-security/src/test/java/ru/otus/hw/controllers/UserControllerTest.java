package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.services.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@SpringBootTest(classes = UserController.class)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc
@DisplayName("Интеграционный тест страницы регистрации")
public class UserControllerTest {
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
