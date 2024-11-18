package com.sparta.nanglangeats.domain.store.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sparta.nanglangeats.domain.store.entity.Store;
import com.sparta.nanglangeats.domain.user.entity.User;

public interface StoreRepository extends JpaRepository<Store, Long> {
	Optional<Store> findByUuid(String uuid);

	Page<Store> findAllByCategoryId(Long categoryId, Pageable pageable);

	// 가게 ID로 주인 찾기
	@Query("SELECT s.owner FROM Store s WHERE s.id = :storeId")
	User findOwnerById(@Param("storeId") Long storeId);

	// 주인으로 가게 ID 찾기
	List<Long> findIdsByOwner(User user);

	// 가게 이름 가져오기
	@Query("SELECT s.name FROM Store s WHERE s.id = :storeId")
	String findNameById(@Param("storeId") Long storeId);
}
