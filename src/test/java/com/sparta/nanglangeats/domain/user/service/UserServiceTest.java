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

import com.sparta.nanglangeats.domain.user.controller.dto.request.UserUpdateRequest;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.domain.user.enums.UserRole;
import com.sparta.nanglangeats.domain.user.repository.UserRepository;
import com.sparta.nanglangeats.domain.user.service.dto.request.ManagerSignupServiceRequest;
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
	@DisplayName("createUser(고객회원가입DTO): 고객회원가입 정보를 입력받아서 유저를 생성한다.")
	void createUser_success() {
		// given
		final UserSignupServiceRequest request = new UserSignupServiceRequest("testerId", "password", "tester", "test@gmail.com", UserRole.CUSTOMER);

		// when
		final Long userId = userService.createUser(request);

		// then
		final Optional<User> result = userRepository.findById(userId);

		assertThat(result.isPresent()).isTrue();
		assertThat(result.get().getUsername()).isEqualTo(request.getUsername());
		assertThat(passwordEncoder.matches(request.getPassword(), result.get().getPassword())).isTrue();
		assertThat(result.get().getNickname()).isEqualTo(request.getNickname());
		assertThat(result.get().getEmail()).isEqualTo(request.getEmail());
		assertThat(result.get().getRole()).isEqualTo(request.getRole());
	}

	@Test
	@DisplayName("createUser(고객회원가입DTO): 고객회원가입 DTO의 유저네임이 중복되면 유저 생성을 실패한다.")
	void createUser_duplicated_username_fail() {
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
		assertThatThrownBy(() -> userService.createUser(request))
			.isInstanceOf(ParameterException.class)
			.hasMessage(COMMON_INVALID_PARAMETER.getMessage())
			.extracting("customFieldErrors")
			.asInstanceOf(InstanceOfAssertFactories.list(CustomFieldError.class))
			.hasSize(1)
			.extracting("rejectedValue", "errorMessage")
			.containsExactly(tuple(duplicatedUsername, DUPLICATED_USERNAME.getMessage()));
	}

	@Test
	@DisplayName("createUser(고객회원가입DTO): 고객회원가입 DTO의 닉네임이 중복되면 유저 생성을 실패한다.")
	void createUser_duplicated_nickname_fail() {
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
		assertThatThrownBy(() -> userService.createUser(request))
			.isInstanceOf(ParameterException.class)
			.hasMessage(COMMON_INVALID_PARAMETER.getMessage())
			.extracting("customFieldErrors")
			.asInstanceOf(InstanceOfAssertFactories.list(CustomFieldError.class))
			.hasSize(1)
			.extracting("rejectedValue", "errorMessage")
			.containsExactly(tuple(duplicatedNickname, DUPLICATED_NICKNAME.getMessage()));
	}

	@Test
	@DisplayName("createUser(고객회원가입DTO): 고객회원가입 DTO의 이메일이 중복되면 유저 생성을 실패한다.")
	void createUser_duplicated_email_fail() {
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
		assertThatThrownBy(() -> userService.createUser(request))
			.isInstanceOf(ParameterException.class)
			.hasMessage(COMMON_INVALID_PARAMETER.getMessage())
			.extracting("customFieldErrors")
			.asInstanceOf(InstanceOfAssertFactories.list(CustomFieldError.class))
			.hasSize(1)
			.extracting("rejectedValue", "errorMessage")
			.containsExactly(tuple(duplicatedEmail, DUPLICATED_EMAIL.getMessage()));
	}

	@Test
	@DisplayName("createUser(고객회원가입DTO): 고객회원가입 DTO의 여러 필드가 중복되면 유저 생성을 실패한다.")
	void createUser_multiple_duplicated_info_fail() {
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
		assertThatThrownBy(() -> userService.createUser(request))
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
	@DisplayName("createUser(매니저회원가입DTO): 매니저 회원가입 정보를 입력받아서 유저를 생성한다.")
	void createManager_success() {
		// given
		final ManagerSignupServiceRequest request = new ManagerSignupServiceRequest("testerId", "password", "tester", "test@gmail.com", UserRole.MANAGER);

		// when
		final Long userId = userService.createManager(request);

		// then
		final Optional<User> result = userRepository.findById(userId);

		assertThat(result.isPresent()).isTrue();
		assertThat(result.get().getUsername()).isEqualTo(request.getUsername());
		assertThat(passwordEncoder.matches(request.getPassword(), result.get().getPassword())).isTrue();
		assertThat(result.get().getNickname()).isEqualTo(request.getNickname());
		assertThat(result.get().getEmail()).isEqualTo(request.getEmail());
		assertThat(result.get().getRole()).isEqualTo(request.getRole());
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
	@DisplayName("updateMyInfo(유저변경정보DTO): 변경 정보를 입력받아 유저를 변경한다.")
	void updateMyInfo_success() {
		// given
		final String username = "testerId";
		final String nickname = "tester";
		final String email = "tester@gmail.com";
		final User savedUser = saveUser(username, nickname, email);

		final String updatePassword = "newPassword";
		final String updateNickname = "tester2";
		final String updateEmail = "tester@gmail.com";
		final Boolean isActive = false;
		final UserUpdateRequest request = new UserUpdateRequest(updatePassword, updateNickname, updateEmail, isActive);

		// when
		Long userId = userService.updateMyInfo(savedUser, request);

		// then
		Optional<User> result = userRepository.findById(userId);
		assertThat(result.isPresent()).isTrue();
		assertThat(passwordEncoder.matches(updatePassword, result.get().getPassword())).isTrue();
		assertThat(result.get().getNickname()).isEqualTo(updateNickname);
		assertThat(result.get().getEmail()).isEqualTo(updateEmail);
		assertThat(result.get().isActive()).isEqualTo(isActive);
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