package com.sparta.nanglangeats.domain.ai.entity;

import java.util.UUID;

import com.sparta.nanglangeats.domain.store.entity.Store;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.global.common.entity.Timestamped;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_ai_data")
public class AiData extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long aiDataId;

	@Column(nullable = false, unique = true)
	private String uuid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(nullable = false, length = 100)
	private String question;

	@Column(nullable = false, length = 100)
	private String answer;

	@Builder
	public AiData(User user, String question, String answer) {
		this.uuid = UUID.randomUUID().toString();
		this.user = user;
		this.question = question;
		this.answer = answer;
	}
}