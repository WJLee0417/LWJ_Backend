package com.stepupbackend.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.stepupbackend.dto.board.BoardDetailResponse;
import com.stepupbackend.exception.UnauthorizedActionException;
import com.stepupbackend.security.MemberUserDetailsService;
import com.stepupbackend.security.SecurityConfig;
import com.stepupbackend.service.BoardService;
import com.stepupbackend.service.CommentService;

@WebMvcTest(BoardController.class)
@Import(SecurityConfig.class)
class BoardControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BoardService boardService;

    @MockitoBean
    private CommentService commentService;

    @MockitoBean
    private MemberUserDetailsService memberUserDetailsService;

    @Test
    @WithMockUser(username = "writer")
    void allowsAuthenticatedUserToCreateBoard() throws Exception {
        when(boardService.createBoard(eq("writer"), any()))
                .thenReturn(new BoardDetailResponse(7L, "자유", "title", "content", "writer", 0, LocalDateTime.now()));

        mockMvc.perform(post("/boards")
                        .with(csrf())
                        .param("category", "자유")
                        .param("title", "title")
                        .param("content", "content"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/boards/7"));

        verify(boardService).createBoard(eq("writer"), any());
    }

    @Test
    @WithMockUser(username = "reader")
    void rejectsAnotherUsersUpdateAndDeleteRequests() throws Exception {
        doThrow(new UnauthorizedActionException("update this board"))
                .when(boardService).updateBoard(eq(1L), eq("reader"), any());
        doThrow(new UnauthorizedActionException("delete this board"))
                .when(boardService).deleteBoard(1L, "reader");

        mockMvc.perform(post("/boards/1")
                        .with(csrf())
                        .param("category", "자유")
                        .param("title", "title")
                        .param("content", "content"))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("이 작업을 수행할 권한이 없습니다.")));

        mockMvc.perform(post("/boards/1/delete").with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("이 작업을 수행할 권한이 없습니다.")));
    }
}
