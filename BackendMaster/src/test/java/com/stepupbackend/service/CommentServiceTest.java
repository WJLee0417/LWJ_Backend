package com.stepupbackend.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.stepupbackend.domain.Board;
import com.stepupbackend.domain.Comment;
import com.stepupbackend.domain.Member;
import com.stepupbackend.exception.UnauthorizedActionException;
import com.stepupbackend.repository.BoardRepository;
import com.stepupbackend.repository.CommentRepository;
import com.stepupbackend.repository.MemberRepository;

class CommentServiceTest {

    private final CommentRepository commentRepository = mock(CommentRepository.class);
    private final BoardRepository boardRepository = mock(BoardRepository.class);
    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final CommentService commentService = new CommentService(commentRepository, boardRepository, memberRepository);

    @Test
    void allowsOnlyCommentAuthorToDelete() {
        Member author = new Member("writer", "hash", "Writer", null);
        Comment comment = new Comment(new Board("자유", "title", "content", author), author, "comment");
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        assertThrows(UnauthorizedActionException.class, () -> commentService.deleteComment(1L, "reader"));
        commentService.deleteComment(1L, "writer");

        verify(commentRepository).delete(comment);
    }
}
