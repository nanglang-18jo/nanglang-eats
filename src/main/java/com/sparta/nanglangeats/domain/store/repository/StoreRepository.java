package com.sparta.nanglangeats.domain.store.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.nanglangeats.domain.store.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
	Optional<Store> findByUuid(String uuid);
}
