package com.stepupbackend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stepupbackend.domain.Member;
import com.stepupbackend.dto.member.MemberSignupRequest;
import com.stepupbackend.exception.DuplicateMemberIdException;
import com.stepupbackend.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Member register(MemberSignupRequest request) {
        if (memberRepository.existsById(request.id())) {
            throw new DuplicateMemberIdException(request.id());
        }

        Member member = new Member(
                request.id(),
                passwordEncoder.encode(request.password()),
                request.name(),
                request.part());
        return memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public boolean isIdAvailable(String memberId) {
        return !memberRepository.existsById(memberId);
    }
}
