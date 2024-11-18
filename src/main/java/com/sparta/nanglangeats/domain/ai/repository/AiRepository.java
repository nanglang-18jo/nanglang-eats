package com.sparta.nanglangeats.domain.ai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.nanglangeats.domain.ai.entity.AiData;

public interface AiRepository extends JpaRepository<AiData, Long> {
}
