package com.sparta.nanglangeats.domain.user.service;

import static com.sparta.nanglangeats.global.common.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.nanglangeats.domain.user.controller.dto.request.UserSignupRequest;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.domain.user.enums.UserRole;
import com.sparta.nanglangeats.domain.user.repository.UserRepository;
import com.sparta.nanglangeats.global.common.exception.CustomException;
import com.sparta.nanglangeats.global.common.exception.CustomFieldError;
import com.sparta.nanglangeats.global.common.exception.ParameterException;

@Transactional
@SpringBootTest
class UserServiceTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	@DisplayName("insertUser(회원가입DTO, 유저역할): 회원가입 정보와 유저 역할을 입력받아서 유저를 생성한다.")
	void insertUser_success() {
		// given
		final UserSignupRequest request = new UserSignupRequest("testerId", "password", "tester", "test@gmail.com");
		final UserRole role = UserRole.CUSTOMER;

		// when
		final Long userId = userService.insertUser(request, role);

		// then
		final Optional<User> result = userRepository.findById(userId);

		assertThat(result.isPresent()).isTrue();
		assertThat(result.get().getUsername()).isEqualTo(request.getUsername());
		assertThat(passwordEncoder.matches(request.getPassword(), result.get().getPassword())).isTrue();
		assertThat(result.get().getNickname()).isEqualTo(request.getNickname());
		assertThat(result.get().getEmail()).isEqualTo(request.getEmail());
		assertThat(result.get().getRole()).isEqualTo(role);
	}

	@Test
	@DisplayName("insertUser(회원가입DTO, 유저역할): 회원가입 DTO의 유저네임이 중복되면 유저 생성을 실패한다.")
	void insertUser_duplicated_username_fail() {
		// given
		final String username = "testerId";
		final String nickname = "tester";
		final String email = "tester@gmail.com";
		saveUser(username, nickname, email);

		final String duplicatedUsername = "testerId";
		final String password2 = "password";
		final String nickname2 = "tester2";
		final String email2 = "tester2@gmail.com";
		final UserRole role = UserRole.CUSTOMER;
		final UserSignupRequest request = new UserSignupRequest(duplicatedUsername, password2, nickname2, email2);

		// expected
		assertThatThrownBy(() -> userService.insertUser(request, role))
			.isInstanceOf(ParameterException.class)
			.hasMessage(COMMON_INVALID_PARAMETER.getMessage())
			.extracting("customFieldErrors")
			.asInstanceOf(InstanceOfAssertFactories.list(CustomFieldError.class))
			.hasSize(1)
			.extracting("rejectedValue", "errorMessage")
			.containsExactly(tuple(duplicatedUsername, DUPLICATED_USERNAME.getMessage()));
	}

	@Test
	@DisplayName("insertUser(회원가입DTO, 유저역할): 회원가입 DTO의 닉네임이 중복되면 유저 생성을 실패한다.")
	void insertUser_duplicated_nickname_fail() {
		// given
		final String username = "testerId";
		final String nickname = "tester";
		final String email = "tester@gmail.com";
		saveUser(username, nickname, email);

		final String username2 = "testerId2";
		final String password2 = "password";
		final String duplicatedNickname = "tester";
		final String email2 = "tester2@gmail.com";
		final UserRole role = UserRole.CUSTOMER;
		final UserSignupRequest request = new UserSignupRequest(username2, password2, duplicatedNickname, email2);

		// expected
		assertThatThrownBy(() -> userService.insertUser(request, role))
			.isInstanceOf(ParameterException.class)
			.hasMessage(COMMON_INVALID_PARAMETER.getMessage())
			.extracting("customFieldErrors")
			.asInstanceOf(InstanceOfAssertFactories.list(CustomFieldError.class))
			.hasSize(1)
			.extracting("rejectedValue", "errorMessage")
			.containsExactly(tuple(duplicatedNickname, DUPLICATED_NICKNAME.getMessage()));
	}

	@Test
	@DisplayName("insertUser(회원가입DTO, 유저역할): 회원가입 DTO의 이메일이 중복되면 유저 생성을 실패한다.")
	void insertUser_duplicated_email_fail() {
		// given
		final String username = "testerId";
		final String nickname = "tester";
		final String email = "tester@gmail.com";
		saveUser(username, nickname, email);

		final String username2 = "testerId2";
		final String password2 = "password";
		final String nickname2 = "tester2";
		final String duplicatedEmail = "tester@gmail.com";
		final UserRole role = UserRole.CUSTOMER;
		final UserSignupRequest request = new UserSignupRequest(username2, password2, nickname2, duplicatedEmail);

		// expected
		assertThatThrownBy(() -> userService.insertUser(request, role))
			.isInstanceOf(ParameterException.class)
			.hasMessage(COMMON_INVALID_PARAMETER.getMessage())
			.extracting("customFieldErrors")
			.asInstanceOf(InstanceOfAssertFactories.list(CustomFieldError.class))
			.hasSize(1)
			.extracting("rejectedValue", "errorMessage")
			.containsExactly(tuple(duplicatedEmail, DUPLICATED_EMAIL.getMessage()));
	}

	@Test
	@DisplayName("insertUser(회원가입DTO, 유저역할): 회원가입 DTO의 여러 필드가 중복되면 유저 생성을 실패한다.")
	void insertUser_multiple_duplicated_info_fail() {
		// given
		final String username = "testerId";
		final String nickname = "tester";
		final String email = "tester@gmail.com";
		saveUser(username, nickname, email);

		final String duplicatedUsername = "testerId";
		final String password2 = "password";
		final String nickname2 = "tester2";
		final String duplicatedEmail = "tester@gmail.com";
		final UserRole role = UserRole.CUSTOMER;
		final UserSignupRequest request = new UserSignupRequest(duplicatedUsername, password2, nickname2, duplicatedEmail);

		// expected
		assertThatThrownBy(() -> userService.insertUser(request, role))
			.isInstanceOf(ParameterException.class)
			.hasMessage(COMMON_INVALID_PARAMETER.getMessage())
			.extracting("customFieldErrors")
			.asInstanceOf(InstanceOfAssertFactories.list(CustomFieldError.class))
			.hasSize(2)
			.extracting("rejectedValue", "errorMessage")
			.containsExactly(
				tuple(duplicatedUsername, DUPLICATED_USERNAME.getMessage()),
				tuple(duplicatedEmail, DUPLICATED_EMAIL.getMessage())
			);
	}

	@Test
	@DisplayName("getUserByUsername(유저네임): 유저네임을 받아 사용자를 조회한다.")
	void getUserByUsername_success() {
		// given
		final String username = "testerId";
		final String nickname = "tester";
		final String email = "tester@gmail.com";
		saveUser(username, nickname, email);

		// when
		User result = userService.getUserByUsername(username);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getUsername()).isEqualTo(username);
		assertThat(result.getNickname()).isEqualTo(nickname);
		assertThat(result.getEmail()).isEqualTo(email);
	}

	@Test
	@DisplayName("getUserByUsername(유저네임): 유저네임이 존재하지 않는 경우 조회에 실패한다.")
	void getUserByUsername_does_not_exist_username_fail() {
		// given
		final String username = "testerId";
		final String nickname = "tester";
		final String email = "tester@gmail.com";
		saveUser(username, nickname, email);

		final String newUsername = "newUsername";

		// expected
		assertThatThrownBy(() -> userService.getUserByUsername(newUsername))
			.isInstanceOf(CustomException.class)
			.hasMessage(USER_NOT_FOUND.getMessage());
	}

	
	private User saveUser(String username, String nickname, String email) {
		return userRepository.save(User.builder()
			.username(username)
			.password("password")
			.nickname(nickname)
			.email(email)
			.role(UserRole.CUSTOMER)
			.build());
	}
}