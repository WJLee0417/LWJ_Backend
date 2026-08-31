package com.stepupbackend.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/** Maps expected web failures to safe, consistent error pages. */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DuplicateMemberIdException.class)
    public String handleDuplicateMemberId(DuplicateMemberIdException exception, HttpServletRequest request) {
        log.warn("Request failed: status=409, type={}, method={}, path={}",
                exception.getClass().getSimpleName(), request.getMethod(), request.getRequestURI());
        return "redirect:/join?error";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(
            ResourceNotFoundException exception, HttpServletRequest request, HttpServletResponse response, Model model) {
        return errorPage(HttpStatus.NOT_FOUND, "요청한 정보를 찾을 수 없습니다.", exception, request, response, model);
    }

    @ExceptionHandler({UnauthorizedActionException.class, AccessDeniedException.class})
    public String handleForbidden(
            RuntimeException exception, HttpServletRequest request, HttpServletResponse response, Model model) {
        return errorPage(HttpStatus.FORBIDDEN, "이 작업을 수행할 권한이 없습니다.", exception, request, response, model);
    }

    @ExceptionHandler({InvalidBoardSearchException.class, MethodArgumentTypeMismatchException.class})
    public String handleBadRequest(
            Exception exception, HttpServletRequest request, HttpServletResponse response, Model model) {
        return errorPage(HttpStatus.BAD_REQUEST, "요청 값을 확인해 주세요.", exception, request, response, model);
    }

    @ExceptionHandler(DataAccessException.class)
    public String handleDatabaseFailure(
            DataAccessException exception, HttpServletRequest request, HttpServletResponse response, Model model) {
        return errorPage(HttpStatus.SERVICE_UNAVAILABLE, "현재 서비스를 이용할 수 없습니다. 잠시 후 다시 시도해 주세요.",
                exception, request, response, model);
    }

    @ExceptionHandler(Exception.class)
    public String handleUnexpectedFailure(
            Exception exception, HttpServletRequest request, HttpServletResponse response, Model model) {
        return errorPage(HttpStatus.INTERNAL_SERVER_ERROR, "요청을 처리하지 못했습니다. 잠시 후 다시 시도해 주세요.",
                exception, request, response, model);
    }

    private String errorPage(
            HttpStatus status,
            String message,
            Exception exception,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model) {
        // Do not log exception messages or stack traces here: database and authentication exceptions
        // can contain connection details or other sensitive values.
        log.warn("Request failed: status={}, type={}, method={}, path={}",
                status.value(), exception.getClass().getSimpleName(), request.getMethod(), request.getRequestURI());
        response.setStatus(status.value());
        model.addAttribute("status", status.value());
        model.addAttribute("title", status.getReasonPhrase());
        model.addAttribute("message", message);
        return "error";
    }
}
