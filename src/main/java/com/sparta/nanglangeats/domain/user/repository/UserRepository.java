package com.sparta.nanglangeats.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.nanglangeats.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByUsername(String username);
	boolean existsByNickname(String nickname);
	boolean existsByEmail(String email);
}
