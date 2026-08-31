package com.stepupbackend.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.stepupbackend.domain.Member;
import com.stepupbackend.repository.MemberRepository;

class MemberUserDetailsServiceTest {

    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final MemberUserDetailsService userDetailsService = new MemberUserDetailsService(memberRepository);

    @Test
    void loadsStoredBcryptHashAsSecurityCredentials() {
        when(memberRepository.findById("member"))
                .thenReturn(Optional.of(new Member("member", "$2a$12$storedBcryptHash", "Member", null)));

        UserDetails userDetails = userDetailsService.loadUserByUsername("member");

        assertEquals("member", userDetails.getUsername());
        assertEquals("$2a$12$storedBcryptHash", userDetails.getPassword());
    }

    @Test
    void returnsGenericAuthenticationFailureForUnknownMember() {
        when(memberRepository.findById("unknown")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("unknown"));

        assertEquals("Authentication failed.", exception.getMessage());
    }
}
