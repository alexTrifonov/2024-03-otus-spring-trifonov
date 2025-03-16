package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.config.SecurityConfig;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.UserService;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {UserController.class, BookControllerTest.class, CommentController.class} )
@Import(SecurityConfig.class)
@DisplayName("Тест доступа к url приложения в зависимости от аутентификации")
public class UrlAccessTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private CommentService commentService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;


    @DisplayName("Метод должен возвращать ожидаемый статус")
    @ParameterizedTest(name = "{0} {1} для user {2} вернет статус {4}")
    @MethodSource("testData")
    public void shouldReturnExpectedStatus(String method, String url, String userName,
                                           String[] roles, int status, boolean checkLoginRedirection) throws Exception {

        var request = method2RequestBuilder(method, url);
        if (nonNull(userName)) {
            request = request.with(user(userName).roles(roles));
        }

        ResultActions resultActions = mockMvc.perform(request)
                .andExpect(status().is(status));

        if (checkLoginRedirection) {
            resultActions.andExpect(redirectedUrlPattern("**/login"));
        }
    }


    private MockHttpServletRequestBuilder method2RequestBuilder(String method, String url) {
        Map<String, Function<String, MockHttpServletRequestBuilder>> methodMap =
                Map.of("get", MockMvcRequestBuilders::get,
                        "post", MockMvcRequestBuilders::post);
        return methodMap.get(method).apply(url);
    }

    public static Stream<Arguments> testData() {
        var roles = new String[]{"USER"};
        return Stream.of(
                Arguments.of("get", "/", "user", roles, 200, false),
                Arguments.of("get", "/register", null, null, 200, false),
                Arguments.of("get", "/", null, null, 302, true),
                Arguments.of("get", "/edit-book/*", null, null, 302, true),
                Arguments.of("post", "/edit-book", null, null, 302, true),
                Arguments.of("post", "/add-book", null, null, 302, true),
                Arguments.of("post", "/delete-book/*", null, null, 302, true),
                Arguments.of("post", "/comments/*", null, null, 302, true)
        );
    }
}
