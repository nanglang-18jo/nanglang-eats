package com.sparta.nanglangeats.domain.refresh_token.entity;

import com.sparta.nanglangeats.domain.user.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name ="p_refresh_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String refreshToken;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserRole role;

	public RefreshToken(String email, String refreshToken, UserRole role) {
		this.email = email;
		this.refreshToken = refreshToken;
		this.role = role;
	}

	public RefreshToken update(String refreshToken) {
		this.refreshToken = refreshToken;
		return this;
	}
}
