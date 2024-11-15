package com.sparta.nanglangeats.domain.ai.entity;

import com.sparta.nanglangeats.domain.store.entity.Store;
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
	private String uuid; // 노출되는 식별자

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	@Column(nullable = false, length = 100)
	private String question;

	@Column(nullable = false, length = 100)
	private String answer;

	@Builder
	public AiData(Store store, String question, String answer) {
		this.store = store;
		this.question = question;
		this.answer = answer;
	}

	public void updateAnswer(String answer) {
		this.answer = answer;
	}
}