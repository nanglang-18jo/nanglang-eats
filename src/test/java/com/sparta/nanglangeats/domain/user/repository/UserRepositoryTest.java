package com.sparta.nanglangeats.domain.user.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.domain.user.enums.UserRole;

@Transactional
@SpringBootTest
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("findByUsername(유저네임): 유저네임을 받아 사용자를 조회한다.")
	void findByUsername() {
		// given
		final String username = "tester";
		final String nickname = "tester";
		final String email = "tester";
		final UserRole role = UserRole.CUSTOMER;

		saveUser(username, nickname, email, role);

		// when
		Optional<User> result = userRepository.findByUsername(username);

		// then
		assertThat(result).isPresent();
		assertThat(result.get().getUsername()).isEqualTo(username);
		assertThat(result.get().getNickname()).isEqualTo(nickname);
		assertThat(result.get().getEmail()).isEqualTo(email);
	}


	@Test
	@DisplayName("existsByUsername(유저네임): 유저네임을 받아 사용자가 존재하는지 확인한다.")
	void existsByUsername() {
		// given
		final String username = "tester";
		final String nickname = "tester";
		final String email = "tester";
		final UserRole role = UserRole.CUSTOMER;

		saveUser(username, nickname, email, role);

		// when
		boolean result = userRepository.existsByUsername(username);

		// then
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("existsByNickname(닉네임): 닉네임을 받아 존재하는지 확인한다.")
	void existsByNickname() {
		// given
		final String username = "tester";
		final String nickname = "tester";
		final String email = "tester@gmail.com";
		final UserRole role = UserRole.CUSTOMER;

		saveUser(username, nickname, email, role);

		// when
		boolean result = userRepository.existsByNickname(nickname);

		// then
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("existsByEmail(이메일): 이메일을 받아 존재하는지 확인한다.")
	void existsByEmail() {
		// given
		final String username = "tester";
		final String nickname = "tester";
		final String email = "tester@gmail.com";
		final UserRole role = UserRole.CUSTOMER;

		saveUser(username, nickname, email, role);

		// when
		boolean result = userRepository.existsByEmail(email);

		// then
		assertThat(result).isTrue();
	}

	private User saveUser(String username, String nickname, String email, UserRole role) {
		return userRepository.save(User.builder()
			.username(username)
			.password("password")
			.nickname(nickname)
			.email(email)
			.role(role)
			.build());
	}
}