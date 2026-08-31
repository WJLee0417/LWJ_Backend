package com.stepupbackend.exception;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.stepupbackend.controller.BoardController;
import com.stepupbackend.security.MemberUserDetailsService;
import com.stepupbackend.security.SecurityConfig;
import com.stepupbackend.service.BoardService;
import com.stepupbackend.service.CommentService;

@WebMvcTest(BoardController.class)
@Import(SecurityConfig.class)
class GlobalExceptionHandlerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BoardService boardService;

    @MockitoBean
    private CommentService commentService;

    @MockitoBean
    private MemberUserDetailsService memberUserDetailsService;

    @Test
    @WithMockUser(username = "member")
    void rendersSafeNotFoundPageForMissingBoard() throws Exception {
        when(boardService.getBoardDetail(99L, "member"))
                .thenThrow(new ResourceNotFoundException("Board", 99L));

        mockMvc.perform(get("/boards/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("요청한 정보를 찾을 수 없습니다.")))
                .andExpect(content().string(containsString("404 Not Found")));
    }
}
