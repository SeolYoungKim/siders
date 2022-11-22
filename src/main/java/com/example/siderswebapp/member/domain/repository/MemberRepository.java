package com.example.siderswebapp.member.domain.repository;

import com.example.siderswebapp.member.domain.AuthId;
import com.example.siderswebapp.member.domain.Member;
import com.example.siderswebapp.member.domain.Name;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByAuthId(AuthId authId);

    Boolean existsByAuthId(AuthId authId);

    Boolean existsByName(Name name);

}
