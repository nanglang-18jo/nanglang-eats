package com.sparta.nanglangeats.domain.user.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLRestriction;

import com.sparta.nanglangeats.domain.user.enums.UserRole;
import com.sparta.nanglangeats.global.common.entity.Timestamped;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name ="p_user")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 10)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, unique = true)
	private String nickname;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserRole role;

	@Column(nullable = false)
	private boolean isActive;

	@Builder
	private User(String username, String password, String nickname, String email, UserRole role) {
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.email = email;
		this.role = role;
		this.isActive = true;
		initCreatedBy(username);
	}

	public void updateUserInfo(String password, String nickname, String email, Boolean isActive) {
		this.password = password;
		this.nickname = nickname;
		this.email = email;
		this.isActive = isActive;
	}

	public void delete(String username) {
		isActive = false;
		this.setDeletedAt(LocalDateTime.now());
		this.setDeletedBy(username);
	}
}
