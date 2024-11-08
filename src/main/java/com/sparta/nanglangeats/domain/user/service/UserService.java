package com.sparta.nanglangeats.domain.user.service;

import static com.sparta.nanglangeats.global.common.exception.ErrorCode.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.nanglangeats.domain.user.controller.dto.request.UserSignupRequest;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.domain.user.enums.UserRole;
import com.sparta.nanglangeats.domain.user.repository.UserRepository;
import com.sparta.nanglangeats.global.common.exception.CustomFieldError;
import com.sparta.nanglangeats.global.common.exception.ParameterException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public Long insertUser(UserSignupRequest request, UserRole role) {
		validateUserInfo(request);

		return userRepository.save(User.builder()
			.username(request.getUsername())
			.password(passwordEncoder.encode(request.getPassword()))
			.nickname(request.getNickname())
			.email(request.getEmail())
			.role(role)
			.build())
			.getId();
	}


	private void validateUserInfo(UserSignupRequest request) {
		List<CustomFieldError> customFieldErrors = new ArrayList<>();

		if (isNotUniqueUsername(request.getUsername())) {
			customFieldErrors.add(new CustomFieldError(request.getUsername(), DUPLICATED_USERNAME));
		}

		if (isNotUniqueNickname(request.getNickname())) {
			customFieldErrors.add(new CustomFieldError(request.getNickname(), DUPLICATED_NICKNAME));
		}

		if (isNotUniqueEmail(request.getEmail())) {
			customFieldErrors.add(new CustomFieldError(request.getEmail(), DUPLICATED_EMAIL));
		}

		if (hasError(customFieldErrors)) {
			throw new ParameterException(COMMON_INVALID_PARAMETER, customFieldErrors);
		}
	}

	@Transactional(readOnly = true)
	protected boolean isNotUniqueUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Transactional(readOnly = true)
	protected boolean isNotUniqueNickname(String nickname) {
		return userRepository.existsByNickname(nickname);
	}

	@Transactional(readOnly = true)
	protected boolean isNotUniqueEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	private boolean hasError(List<CustomFieldError> customFieldErrors) {
		return !customFieldErrors.isEmpty();
	}
}
