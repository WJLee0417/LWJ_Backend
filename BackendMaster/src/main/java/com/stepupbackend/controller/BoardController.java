package com.stepupbackend.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

import com.stepupbackend.dto.board.BoardCreateRequest;
import com.stepupbackend.dto.board.BoardDetailResponse;
import com.stepupbackend.dto.board.BoardPageResponse;
import com.stepupbackend.dto.board.BoardSearchRequest;
import com.stepupbackend.dto.board.BoardUpdateRequest;
import com.stepupbackend.dto.comment.CommentCreateRequest;
import com.stepupbackend.service.BoardService;
import com.stepupbackend.service.CommentService;

@Controller
public class BoardController {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private final BoardService boardService;
    private final CommentService commentService;

    public BoardController(BoardService boardService, CommentService commentService) {
        this.boardService = boardService;
        this.commentService = commentService;
    }

    @GetMapping("/boards")
    public String listBoards(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        int requestedPage = Math.max(page, 1);
        BoardPageResponse boardPage = boardService.getBoardPage(
                new BoardSearchRequest(category, searchType, keyword, requestedPage - 1, DEFAULT_PAGE_SIZE));
        model.addAttribute("boardPage", boardPage);
        model.addAttribute("category", category == null ? "전체" : category);
        model.addAttribute("searchType", searchType == null ? "title" : searchType);
        model.addAttribute("keyword", keyword == null ? "" : keyword);
        return "boards/list";
    }

    @GetMapping("/boards/new")
    public String newBoardForm(Model model) {
        populateFormModel(model, new BoardCreateRequest("학습", "", ""), "/boards", "새 글 작성");
        return "boards/form";
    }

    @PostMapping("/boards")
    public String createBoard(
            @AuthenticationPrincipal UserDetails user,
            @Valid @ModelAttribute("boardForm") BoardCreateRequest request,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            populateFormModel(model, request, "/boards", "새 글 작성");
            return "boards/form";
        }
        BoardDetailResponse board = boardService.createBoard(user.getUsername(), request);
        return "redirect:/boards/" + board.id();
    }

    @GetMapping("/boards/{boardId}")
    public String boardDetail(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetails user,
            Model model) {
        BoardDetailResponse board = boardService.getBoardDetail(boardId, user.getUsername());
        populateDetailModel(model, board, user.getUsername());
        return "boards/detail";
    }

    @GetMapping("/boards/{boardId}/edit")
    public String editBoardForm(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetails user,
            Model model) {
        BoardDetailResponse board = boardService.getBoardForEdit(boardId, user.getUsername());
        BoardUpdateRequest request = new BoardUpdateRequest(board.category(), board.title(), board.content());
        populateFormModel(model, request, "/boards/" + boardId, "게시글 수정");
        return "boards/form";
    }

    @PostMapping("/boards/{boardId}")
    public String updateBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetails user,
            @Valid @ModelAttribute("boardForm") BoardUpdateRequest request,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            populateFormModel(model, request, "/boards/" + boardId, "게시글 수정");
            return "boards/form";
        }
        boardService.updateBoard(boardId, user.getUsername(), request);
        return "redirect:/boards/" + boardId;
    }

    @PostMapping("/boards/{boardId}/delete")
    public String deleteBoard(@PathVariable Long boardId, @AuthenticationPrincipal UserDetails user) {
        boardService.deleteBoard(boardId, user.getUsername());
        return "redirect:/boards";
    }

    @PostMapping("/boards/{boardId}/comments")
    public String createComment(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetails user,
            @Valid @ModelAttribute("commentForm") CommentCreateRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/boards/" + boardId + "?commentError";
        }
        commentService.createComment(boardId, user.getUsername(), request);
        return "redirect:/boards/" + boardId;
    }

    @PostMapping("/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetails user) {
        Long boardId = commentService.deleteComment(commentId, user.getUsername());
        return "redirect:/boards/" + boardId;
    }

    private void populateDetailModel(Model model, BoardDetailResponse board, String currentUserId) {
        model.addAttribute("board", board);
        model.addAttribute("comments", commentService.getComments(board.id()));
        model.addAttribute("commentForm", new CommentCreateRequest(""));
        model.addAttribute("currentUserId", currentUserId);
    }

    private static void populateFormModel(Model model, Object boardForm, String formAction, String formTitle) {
        model.addAttribute("boardForm", boardForm);
        model.addAttribute("formAction", formAction);
        model.addAttribute("formTitle", formTitle);
    }
}
