package com.sparta.nanglangeats.domain.store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sparta.nanglangeats.domain.store.entity.Store;

public interface StoreRepositoryCustom {
	Page<Store> searchStoreByKeyword(String keyword, Pageable pageable);
}
