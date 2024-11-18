package com.sparta.nanglangeats.domain.refresh_token.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.nanglangeats.domain.refresh_token.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByEmail(String email);
	Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
