package com.stepupbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stepupbackend.domain.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
}
