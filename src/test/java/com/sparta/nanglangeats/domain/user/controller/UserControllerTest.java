package com.sparta.nanglangeats.domain.user.controller;

import static org.springframework.http.MediaType.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.nanglangeats.domain.user.controller.dto.request.ManagerSignupRequest;
import com.sparta.nanglangeats.domain.user.controller.dto.request.UserSignupRequest;
import com.sparta.nanglangeats.domain.user.controller.dto.request.UserUpdateRequest;
import com.sparta.nanglangeats.domain.user.enums.UserRole;
import com.sparta.nanglangeats.domain.user.service.UserService;

@WithMockUser
@WebMvcTest(UserController.class)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UserService userService;

	@Test
	@DisplayName("createUser(회원가입DTO): 회원가입 정보를 입력받아 유저를 생성한다.")
	void createUser_success() throws Exception {
		// given
		final String uri = "/api/users/signup";
		final UserSignupRequest request = new UserSignupRequest("tester12", "password", "nickname", "tester@gmail.com", UserRole.CUSTOMER);

		// expected
		mockMvc.perform(post(uri).with(csrf())
			.contentType(APPLICATION_JSON_VALUE)
			.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("createUser(회원가입DTO): username 길이가 4자 이상 10자 이하가 아니라면 실패한다.")
	void createUser_username_length_validation() throws Exception {
		// given
		final String uri = "/api/users/signup";
		final String username = "customer123";
		final UserSignupRequest request = new UserSignupRequest(username, "password", "nickname", "tester@gmail.com", UserRole.CUSTOMER);

		// expected
		mockMvc.perform(post(uri).with(csrf())
				.contentType(APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("username : 아이디를 확인해 주세요."));
	}

	@Test
	@DisplayName("createUser(회원가입DTO): username 이 소문자 알파벳 + 숫자가 아니라면 실패한다.")
	void createUser_username_lowercase_alphanumeric_validation() throws Exception {
		// given
		final String uri = "/api/users/signup";
		final String username = "UPPERCASE!";
		final UserSignupRequest request = new UserSignupRequest(username, "password", "nickname", "tester@gmail.com", UserRole.CUSTOMER);

		// expected
		mockMvc.perform(post(uri).with(csrf())
				.contentType(APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("username : 아이디를 확인해 주세요."));
	}

	@Test
	@DisplayName("createUser(회원가입DTO): password 길이가 8자 이상 15자 이하가 아니라면 실패한다.")
	void createUser_password_length_validation() throws Exception {
		// given
		final String uri = "/api/users/signup";
		final String password = "passwor";
		final UserSignupRequest request = new UserSignupRequest("tester123", password, "nickname", "tester@gmail.com", UserRole.CUSTOMER);

		// expected
		mockMvc.perform(post(uri).with(csrf())
				.contentType(APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("password : 8자 이상의 15자 이하의 숫자, 대/소문자, 특수문자를 포함한 비밀번호를 입력해주세요."));
	}

	@Test
	@DisplayName("createUser(회원가입DTO): nickname 이 null 이면 실패한다.")
	void createUser_nickname_null_validation() throws Exception {
		// given
		final String uri = "/api/users/signup";
		final UserSignupRequest request = new UserSignupRequest("tester123", "password", null, "tester@gmail.com", UserRole.CUSTOMER);

		// expected
		mockMvc.perform(post(uri).with(csrf())
				.contentType(APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("nickname : 최소 1자 이상의 닉네임을 반드시 입력해 주세요."));
	}

	@Test
	@DisplayName("createUser(회원가입DTO): nickname 이 1자 미만이면 실패한다.")
	void createUser_nickname_min_validation() throws Exception {
		// given
		final String uri = "/api/users/signup";
		final String nickname = "   ";
		final UserSignupRequest request = new UserSignupRequest("tester123", "password", nickname, "tester@gmail.com", UserRole.CUSTOMER);

		// expected
		mockMvc.perform(post(uri).with(csrf())
				.contentType(APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("nickname : 최소 1자 이상의 닉네임을 반드시 입력해 주세요."));
	}

	@Test
	@DisplayName("createUser(회원가입DTO): email 이 정규식을 준수하지 않으면 실패한다.")
	void createUser_email_regex_validation() throws Exception {
		// given
		final String uri = "/api/users/signup";
		final String email = "tester!email.com";
		final UserSignupRequest request = new UserSignupRequest("tester123", "password", "nickname", email, UserRole.CUSTOMER);

		// expected
		mockMvc.perform(post(uri).with(csrf())
				.contentType(APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("email : 이메일을 확인해 주세요."));
	}

	@Test
	@DisplayName("createUser(회원가입DTO): role 이 null이면 실패한다.")
	void createUser_role_null_validation() throws Exception {
		// given
		final String uri = "/api/users/signup";
		final UserSignupRequest request = new UserSignupRequest("tester123", "password", "nickname", "tester@gmail.com", null);

		// expected
		mockMvc.perform(post(uri).with(csrf())
				.contentType(APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("role : 유저의 권한을 선택해 주세요."));
	}

	@Test
	@DisplayName("createManager(회원가입DTO): 회원가입 정보를 입력받아 매니저를 생성한다.")
	void createManager_success() throws Exception {
		// given
		final String uri = "/api/admin/managers";
		final ManagerSignupRequest request = new ManagerSignupRequest("tester12", "password", "nickname", "tester@gmail.com");

		// expected
		mockMvc.perform(post(uri).with(csrf())
				.contentType(APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("updateUser(유저변경정보DTO): 변경 정보를 입력받아 유저를 변경한다.")
	void updateUser() throws Exception {
		// given
		final String uri = "/api/users";
		final UserUpdateRequest request = new UserUpdateRequest("newPassword", "updateNick", "tester@gmail.com", false);

		// expected
		mockMvc.perform(put(uri).with(csrf())
				.contentType(APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpect(status().isOk());
	}
}