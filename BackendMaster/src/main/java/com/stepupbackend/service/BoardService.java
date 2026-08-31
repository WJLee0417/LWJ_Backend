package com.stepupbackend.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stepupbackend.domain.Board;
import com.stepupbackend.domain.Member;
import com.stepupbackend.dto.board.BoardCreateRequest;
import com.stepupbackend.dto.board.BoardDetailResponse;
import com.stepupbackend.dto.board.BoardPageResponse;
import com.stepupbackend.dto.board.BoardSearchRequest;
import com.stepupbackend.dto.board.BoardSummaryResponse;
import com.stepupbackend.dto.board.BoardUpdateRequest;
import com.stepupbackend.exception.InvalidBoardSearchException;
import com.stepupbackend.exception.ResourceNotFoundException;
import com.stepupbackend.exception.UnauthorizedActionException;
import com.stepupbackend.repository.BoardRepository;
import com.stepupbackend.repository.MemberRepository;

@Service
public class BoardService {

    private static final String NOTICE_CATEGORY = "공지";
    private static final String ALL_CATEGORY = "전체";
    private static final Set<String> SEARCH_TYPES = Set.of("title", "content", "author");
    private static final int MAX_PAGE_SIZE = 50;

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public BoardService(BoardRepository boardRepository, MemberRepository memberRepository) {
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public BoardPageResponse getBoardPage(BoardSearchRequest request) {
        ValidatedSearch search = validateSearch(request);
        List<BoardSummaryResponse> notices = boardRepository.findByCategoryOrderByIdDesc(NOTICE_CATEGORY).stream()
                .map(BoardSummaryResponse::from)
                .toList();
        Page<Board> boards = boardRepository.findRegularBoards(
                NOTICE_CATEGORY,
                search.category(),
                search.searchType(),
                search.keyword(),
                PageRequest.of(search.page(), search.size(), Sort.by(Sort.Direction.DESC, "id")));

        return new BoardPageResponse(
                notices,
                boards.map(BoardSummaryResponse::from).getContent(),
                boards.getNumber(),
                boards.getSize(),
                boards.getTotalPages(),
                boards.getTotalElements());
    }

    @Transactional
    public BoardDetailResponse getBoardDetail(Long boardId, String requesterId) {
        Board board = getBoard(boardId);
        if (requesterId != null && !requesterId.isBlank() && !board.isWrittenBy(requesterId)) {
            board.incrementViews();
        }
        return BoardDetailResponse.from(board);
    }

    @Transactional(readOnly = true)
    public BoardDetailResponse getBoardForEdit(Long boardId, String requesterId) {
        Board board = getBoard(boardId);
        requireBoardAuthor(board, requesterId, "edit this board");
        return BoardDetailResponse.from(board);
    }

    @Transactional
    public BoardDetailResponse createBoard(String authorId, BoardCreateRequest request) {
        Member author = memberRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Member", authorId));
        Board board = boardRepository.save(new Board(request.category(), request.title(), request.content(), author));
        return BoardDetailResponse.from(board);
    }

    @Transactional
    public BoardDetailResponse updateBoard(Long boardId, String requesterId, BoardUpdateRequest request) {
        Board board = getBoard(boardId);
        requireBoardAuthor(board, requesterId, "update this board");
        board.update(request.category(), request.title(), request.content());
        return BoardDetailResponse.from(board);
    }

    @Transactional
    public void deleteBoard(Long boardId, String requesterId) {
        Board board = getBoard(boardId);
        requireBoardAuthor(board, requesterId, "delete this board");
        boardRepository.delete(board);
    }

    private Board getBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board", boardId));
    }

    private static void requireBoardAuthor(Board board, String requesterId, String action) {
        if (requesterId == null || requesterId.isBlank() || !board.isWrittenBy(requesterId)) {
            throw new UnauthorizedActionException(action);
        }
    }

    private static ValidatedSearch validateSearch(BoardSearchRequest request) {
        if (request.page() < 0) {
            throw new InvalidBoardSearchException("Page must not be negative.");
        }
        if (request.size() < 1 || request.size() > MAX_PAGE_SIZE) {
            throw new InvalidBoardSearchException("Page size must be between 1 and " + MAX_PAGE_SIZE + ".");
        }

        String category = normalizeCategory(request.category());
        String keyword = normalizeKeyword(request.keyword());
        if (keyword == null) {
            return new ValidatedSearch(category, null, null, request.page(), request.size());
        }
        if (!SEARCH_TYPES.contains(request.searchType())) {
            throw new InvalidBoardSearchException("Unsupported search type.");
        }
        return new ValidatedSearch(category, request.searchType(), keyword, request.page(), request.size());
    }

    private static String normalizeCategory(String category) {
        if (category == null || category.isBlank() || ALL_CATEGORY.equals(category)) {
            return null;
        }
        return category;
    }

    private static String normalizeKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        return keyword.trim();
    }

    private record ValidatedSearch(String category, String searchType, String keyword, int page, int size) {
    }
}
