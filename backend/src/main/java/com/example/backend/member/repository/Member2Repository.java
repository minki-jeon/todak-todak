package com.example.backend.member.repository;

import com.example.backend.member.dto.MemberListInfo;
import com.example.backend.member.entity.Member2;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface Member2Repository extends JpaRepository<Member2, Integer> {

    List<MemberListInfo> findAllBy();

    Optional<Member2> findByMemberId(String memberId);
}