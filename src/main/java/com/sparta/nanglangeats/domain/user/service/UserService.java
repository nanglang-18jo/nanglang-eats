package com.sparta.nanglangeats.domain.user.service;

import static com.sparta.nanglangeats.global.common.exception.ErrorCode.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.domain.user.repository.UserRepository;
import com.sparta.nanglangeats.domain.user.service.dto.request.UserSignupServiceRequest;
import com.sparta.nanglangeats.global.common.exception.CustomException;
import com.sparta.nanglangeats.global.common.exception.CustomFieldError;
import com.sparta.nanglangeats.global.common.exception.ParameterException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public Long createUser(UserSignupServiceRequest request) {
		validateUserInfo(request.getUsername(), request.getNickname(), request.getEmail());

		return userRepository.save(User.builder()
			.username(request.getUsername())
			.password(passwordEncoder.encode(request.getPassword()))
			.nickname(request.getNickname())
			.email(request.getEmail())
			.role(request.getRole())
			.build()).getId();
	}

	@Transactional(readOnly = true)
	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username)
			.orElseThrow(() -> new CustomException(USER_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public User getUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(USER_NOT_FOUND));
	}

	private void validateUserInfo(String username, String nickname, String email) {
		List<CustomFieldError> customFieldErrors = new ArrayList<>();

		if (isNotUniqueUsername(username)) {
			customFieldErrors.add(new CustomFieldError(username, DUPLICATED_USERNAME));
		}

		if (isNotUniqueNickname(nickname)) {
			customFieldErrors.add(new CustomFieldError(nickname, DUPLICATED_NICKNAME));
		}

		if (isNotUniqueEmail(email)) {
			customFieldErrors.add(new CustomFieldError(email, DUPLICATED_EMAIL));
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
