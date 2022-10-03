package com.example.siderswebapp.repository.member;

import com.example.siderswebapp.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByAuthId(String authId);

    Boolean existsByAuthId(String authId);

    Boolean existsByName(String name);
}
