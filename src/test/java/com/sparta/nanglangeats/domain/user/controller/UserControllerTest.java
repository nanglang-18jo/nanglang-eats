package com.sparta.nanglangeats.domain.user.controller;

import static org.springframework.http.MediaType.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.nanglangeats.domain.user.controller.dto.request.UserSignupRequest;
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

	@BeforeEach
	void setUp() {
		PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(1L, null, List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER")));
		SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(authentication);
	}

	@Test
	@DisplayName("create(고객회원가입DTO): ")
	void create() throws Exception {
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

}