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

import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.domain.user.enums.UserRole;
import com.sparta.nanglangeats.domain.user.repository.UserRepository;
import com.sparta.nanglangeats.domain.user.service.dto.request.UserSignupServiceRequest;
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
	@DisplayName("create(고객회원가입DTO): 고객회원가입 정보를 입력받아서 유저를 생성한다.")
	void create_success() {
		// given
		final UserSignupServiceRequest request = new UserSignupServiceRequest("testerId", "password", "tester", "test@gmail.com", UserRole.CUSTOMER);
		final UserRole role = UserRole.CUSTOMER;

		// when
		final Long userId = userService.create(request);

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
	@DisplayName("create(고객회원가입DTO): 고객회원가입 DTO의 유저네임이 중복되면 유저 생성을 실패한다.")
	void create_duplicated_username_fail() {
		// given
		final String username = "testerId";
		final String nickname = "tester";
		final String email = "tester@gmail.com";
		saveUser(username, nickname, email);

		final String duplicatedUsername = "testerId";
		final String password2 = "password";
		final String nickname2 = "tester2";
		final String email2 = "tester2@gmail.com";
		final UserSignupServiceRequest request = new UserSignupServiceRequest(duplicatedUsername, password2, nickname2, email2, UserRole.CUSTOMER);

		// expected
		assertThatThrownBy(() -> userService.create(request))
			.isInstanceOf(ParameterException.class)
			.hasMessage(COMMON_INVALID_PARAMETER.getMessage())
			.extracting("customFieldErrors")
			.asInstanceOf(InstanceOfAssertFactories.list(CustomFieldError.class))
			.hasSize(1)
			.extracting("rejectedValue", "errorMessage")
			.containsExactly(tuple(duplicatedUsername, DUPLICATED_USERNAME.getMessage()));
	}

	@Test
	@DisplayName("create(고객회원가입DTO): 고객회원가입 DTO의 닉네임이 중복되면 유저 생성을 실패한다.")
	void create_duplicated_nickname_fail() {
		// given
		final String username = "testerId";
		final String nickname = "tester";
		final String email = "tester@gmail.com";
		saveUser(username, nickname, email);

		final String username2 = "testerId2";
		final String password2 = "password";
		final String duplicatedNickname = "tester";
		final String email2 = "tester2@gmail.com";
		final UserSignupServiceRequest request = new UserSignupServiceRequest(username2, password2, duplicatedNickname, email2, UserRole.CUSTOMER);

		// expected
		assertThatThrownBy(() -> userService.create(request))
			.isInstanceOf(ParameterException.class)
			.hasMessage(COMMON_INVALID_PARAMETER.getMessage())
			.extracting("customFieldErrors")
			.asInstanceOf(InstanceOfAssertFactories.list(CustomFieldError.class))
			.hasSize(1)
			.extracting("rejectedValue", "errorMessage")
			.containsExactly(tuple(duplicatedNickname, DUPLICATED_NICKNAME.getMessage()));
	}

	@Test
	@DisplayName("create(고객회원가입DTO): 고객회원가입 DTO의 이메일이 중복되면 유저 생성을 실패한다.")
	void create_duplicated_email_fail() {
		// given
		final String username = "testerId";
		final String nickname = "tester";
		final String email = "tester@gmail.com";
		saveUser(username, nickname, email);

		final String username2 = "testerId2";
		final String password2 = "password";
		final String nickname2 = "tester2";
		final String duplicatedEmail = "tester@gmail.com";
		final UserSignupServiceRequest request = new UserSignupServiceRequest(username2, password2, nickname2, duplicatedEmail, UserRole.CUSTOMER);

		// expected
		assertThatThrownBy(() -> userService.create(request))
			.isInstanceOf(ParameterException.class)
			.hasMessage(COMMON_INVALID_PARAMETER.getMessage())
			.extracting("customFieldErrors")
			.asInstanceOf(InstanceOfAssertFactories.list(CustomFieldError.class))
			.hasSize(1)
			.extracting("rejectedValue", "errorMessage")
			.containsExactly(tuple(duplicatedEmail, DUPLICATED_EMAIL.getMessage()));
	}

	@Test
	@DisplayName("create(고객회원가입DTO): 고객회원가입 DTO의 여러 필드가 중복되면 유저 생성을 실패한다.")
	void create_multiple_duplicated_info_fail() {
		// given
		final String username = "testerId";
		final String nickname = "tester";
		final String email = "tester@gmail.com";
		saveUser(username, nickname, email);

		final String duplicatedUsername = "testerId";
		final String password2 = "password";
		final String nickname2 = "tester2";
		final String duplicatedEmail = "tester@gmail.com";
		final UserSignupServiceRequest request = new UserSignupServiceRequest(duplicatedUsername, password2, nickname2, duplicatedEmail, UserRole.CUSTOMER);

		// expected
		assertThatThrownBy(() -> userService.create(request))
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

	@Test
	@DisplayName("getUserById(유저PK): 유저PK를 받아 사용자를 조회한다.")
	void getUserById_success() {
		// given
		final String username = "testerId";
		final String nickname = "tester";
		final String email = "tester@gmail.com";

		final User savedUser = saveUser(username, nickname, email);

		// when
		User findUser = userService.getUserById(savedUser.getId());

		// then
		assertThat(findUser).isEqualTo(savedUser);
		assertThat(findUser.getUsername()).isEqualTo(username);
		assertThat(findUser.getNickname()).isEqualTo(nickname);
		assertThat(findUser.getEmail()).isEqualTo(email);
	}

	@Test
	@DisplayName("getUserById(유저PK): 유저PK가 존재하지 않는 경우 조회에 실패한다.")
	void getUserById_does_not_exist_id_fail() {
		// given
		final String username = "testerId";
		final String nickname = "tester";
		final String email = "tester@gmail.com";
		saveUser(username, nickname, email);

		final Long WrongUserId = Long.valueOf(userRepository.count() + 1L);

		// when
		assertThatThrownBy(() -> userService.getUserById(WrongUserId))
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