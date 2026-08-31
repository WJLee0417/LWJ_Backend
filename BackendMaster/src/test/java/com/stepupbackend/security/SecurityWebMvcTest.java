package com.stepupbackend.security;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.stepupbackend.controller.AuthController;
import com.stepupbackend.service.MemberService;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class SecurityWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private MemberUserDetailsService memberUserDetailsService;

    @Test
    void redirectsUnauthenticatedProtectedUrlToLogin() throws Exception {
        mockMvc.perform(get("/boards/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    void rejectsStateChangingSignupWithoutCsrfToken() throws Exception {
        mockMvc.perform(post("/join")
                        .param("id", "new-user")
                        .param("password", "password")
                        .param("name", "New User"))
                .andExpect(status().isForbidden());
    }

    @Test
    void authenticatesWithSecurityFormLoginAndReturnsGenericFailureMessage() throws Exception {
        String hash = new BCryptPasswordEncoder().encode("correct-password");
        UserDetails user = User.withUsername("member").password(hash).roles("USER").build();
        when(memberUserDetailsService.loadUserByUsername("member")).thenReturn(user);

        MvcResult loginResult = mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("id", "member")
                        .param("password", "correct-password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/boards"))
                .andReturn();

        assertNotNull(loginResult.getRequest().getSession(false));
        assertNotNull(loginResult.getRequest().getSession(false)
                .getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY));

        mockMvc.perform(get("/login").param("error", ""))
                .andExpect(status().isOk())
                .andExpect(content().string("아이디 또는 비밀번호가 일치하지 않습니다."));
    }
}
