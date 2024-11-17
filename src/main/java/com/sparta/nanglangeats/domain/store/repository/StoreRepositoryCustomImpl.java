package com.sparta.nanglangeats.domain.store.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.sparta.nanglangeats.domain.store.entity.QStore.store;

import com.sparta.nanglangeats.domain.store.entity.Store;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StoreRepositoryCustomImpl implements StoreRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Store> findByKeyword(String keyword, Pageable pageable) {
		List<Store> stores = queryFactory.selectFrom(store)
			.where(store.name.containsIgnoreCase(keyword))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(store.name.asc())
			.fetch();

		long totalCount = queryFactory.selectFrom(store)
			.where(store.name.containsIgnoreCase(keyword))
			.fetchCount();

		return new PageImpl<>(stores, pageable, totalCount);
	}
}
