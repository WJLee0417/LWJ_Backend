package com.stepupbackend.exception;

public class DuplicateMemberIdException extends RuntimeException {

    public DuplicateMemberIdException(String memberId) {
        super("Member ID is already in use: " + memberId);
    }
}
