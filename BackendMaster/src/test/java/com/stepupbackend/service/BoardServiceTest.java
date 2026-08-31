package com.stepupbackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.stepupbackend.domain.Board;
import com.stepupbackend.domain.Member;
import com.stepupbackend.dto.board.BoardPageResponse;
import com.stepupbackend.dto.board.BoardSearchRequest;
import com.stepupbackend.exception.InvalidBoardSearchException;
import com.stepupbackend.exception.UnauthorizedActionException;
import com.stepupbackend.repository.BoardRepository;
import com.stepupbackend.repository.MemberRepository;

class BoardServiceTest {

    private final BoardRepository boardRepository = mock(BoardRepository.class);
    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final BoardService boardService = new BoardService(boardRepository, memberRepository);

    @Test
    void separatesNoticesFromRegularSearchPage() {
        Member author = new Member("writer", "hash", "Writer", null);
        Board notice = new Board("공지", "notice", "content", author);
        Board regular = new Board("자유", "regular", "content", author);
        when(boardRepository.findByCategoryOrderByIdDesc("공지")).thenReturn(List.of(notice));
        when(boardRepository.findRegularBoards(eq("공지"), eq(null), eq("title"), eq("regular"), any()))
                .thenReturn(new PageImpl<>(List.of(regular), PageRequest.of(0, 10), 1));

        BoardPageResponse result = boardService.getBoardPage(new BoardSearchRequest("전체", "title", "regular", 0, 10));

        assertEquals(1, result.notices().size());
        assertEquals(1, result.boards().size());
        assertEquals(1, result.totalElements());
    }

    @Test
    void rejectsUnsupportedSearchTypeAndInvalidPageSize() {
        assertThrows(InvalidBoardSearchException.class,
                () -> boardService.getBoardPage(new BoardSearchRequest("전체", "unknown", "keyword", 0, 10)));
        assertThrows(InvalidBoardSearchException.class,
                () -> boardService.getBoardPage(new BoardSearchRequest("전체", "title", "keyword", 0, 51)));
    }

    @Test
    void incrementsViewsOnlyForLoggedInNonAuthorAndProtectsDeletion() {
        Member author = new Member("writer", "hash", "Writer", null);
        Board board = new Board("자유", "title", "content", author);
        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        boardService.getBoardDetail(1L, "writer");
        assertEquals(0, board.getViews());
        boardService.getBoardDetail(1L, "reader");
        assertEquals(1, board.getViews());

        assertThrows(UnauthorizedActionException.class, () -> boardService.deleteBoard(1L, "reader"));
        boardService.deleteBoard(1L, "writer");
        verify(boardRepository).delete(board);
    }
}
