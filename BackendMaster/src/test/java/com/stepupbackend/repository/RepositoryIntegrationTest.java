package com.stepupbackend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.stepupbackend.domain.Board;
import com.stepupbackend.domain.Comment;
import com.stepupbackend.domain.Member;

@SpringBootTest
@Transactional
@EnabledIfEnvironmentVariable(named = "DB_URL", matches = ".+")
class RepositoryIntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void repositoriesPersistAndQueryMemberBoardAndComment() {
        Member member = memberRepository.save(new Member("repository-user", "$2a$12$repositoryTestHash", "Repository User", "backend"));
        Board board = boardRepository.save(new Board("자유", "repository test board", "repository test content", member));
        commentRepository.save(new Comment(board, member, "repository test comment"));

        assertEquals(2, boardRepository.findByCategoryOrderByIdDesc("공지").size());

        Page<Board> boards = boardRepository.findRegularBoards(
                "공지", null, "title", "repository test", PageRequest.of(0, 10));
        assertEquals(1, boards.getTotalElements());
        assertEquals(0, boards.getContent().get(0).getViews());

        List<Comment> comments = commentRepository.findByBoardIdOrderByIdAsc(board.getId());
        assertEquals(1, comments.size());
        assertTrue(memberRepository.findById("repository-user").isPresent());
    }
}
