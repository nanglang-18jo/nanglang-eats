package com.sparta.nanglangeats.domain.store.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.nanglangeats.domain.store.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom {
	Optional<Store> findByUuid(String uuid);

	Page<Store> findAllByCategoryId(Long categoryId, Pageable pageable);
}
