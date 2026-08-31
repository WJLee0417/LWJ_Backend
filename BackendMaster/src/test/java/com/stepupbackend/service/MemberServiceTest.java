package com.stepupbackend.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.stepupbackend.domain.Member;
import com.stepupbackend.dto.member.MemberSignupRequest;
import com.stepupbackend.exception.DuplicateMemberIdException;
import com.stepupbackend.repository.MemberRepository;

class MemberServiceTest {

    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final MemberService memberService = new MemberService(memberRepository, passwordEncoder);

    @Test
    void registersOnlyAvailableIdWithEncodedPassword() {
        MemberSignupRequest request = new MemberSignupRequest("new-user", "plain-password", "New User", "backend");
        when(memberRepository.existsById("new-user")).thenReturn(false);
        when(passwordEncoder.encode("plain-password")).thenReturn("bcrypt-hash");
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        memberService.register(request);

        ArgumentCaptor<Member> savedMember = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository).save(savedMember.capture());
        verify(passwordEncoder).encode("plain-password");
        assertTrue("bcrypt-hash".equals(savedMember.getValue().getPasswordHash()));
    }

    @Test
    void rejectsDuplicateIdAndReportsAvailability() {
        when(memberRepository.existsById("duplicate")).thenReturn(true);

        assertThrows(DuplicateMemberIdException.class,
                () -> memberService.register(new MemberSignupRequest("duplicate", "password", "User", null)));
        assertFalse(memberService.isIdAvailable("duplicate"));
    }
}
