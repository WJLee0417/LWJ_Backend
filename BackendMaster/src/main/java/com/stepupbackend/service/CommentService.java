package com.stepupbackend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stepupbackend.domain.Board;
import com.stepupbackend.domain.Comment;
import com.stepupbackend.domain.Member;
import com.stepupbackend.dto.comment.CommentCreateRequest;
import com.stepupbackend.dto.comment.CommentResponse;
import com.stepupbackend.exception.ResourceNotFoundException;
import com.stepupbackend.exception.UnauthorizedActionException;
import com.stepupbackend.repository.BoardRepository;
import com.stepupbackend.repository.CommentRepository;
import com.stepupbackend.repository.MemberRepository;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public CommentService(
            CommentRepository commentRepository,
            BoardRepository boardRepository,
            MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public CommentResponse createComment(Long boardId, String authorId, CommentCreateRequest request) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board", boardId));
        Member author = memberRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Member", authorId));
        return CommentResponse.from(commentRepository.save(new Comment(board, author, request.content())));
    }

    @Transactional
    public Long deleteComment(Long commentId, String requesterId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", commentId));
        if (requesterId == null || requesterId.isBlank() || !comment.isWrittenBy(requesterId)) {
            throw new UnauthorizedActionException("delete this comment");
        }
        Long boardId = comment.getBoard().getId();
        commentRepository.delete(comment);
        return boardId;
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long boardId) {
        return commentRepository.findByBoardIdOrderByIdAsc(boardId).stream()
                .map(CommentResponse::from)
                .toList();
    }
}
