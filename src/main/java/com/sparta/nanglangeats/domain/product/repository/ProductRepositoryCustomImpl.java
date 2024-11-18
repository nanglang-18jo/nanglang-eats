package com.sparta.nanglangeats.domain.product.repository;

import static com.sparta.nanglangeats.domain.store.entity.QStore.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.nanglangeats.domain.product.entity.Product;
import static com.sparta.nanglangeats.domain.product.entity.QProduct.product;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Product> searchProductByKeyword(String keyword, Pageable pageable) {
		List<Product> products = queryFactory.selectFrom(product)
			.where(product.name.containsIgnoreCase(keyword))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(product.name.asc())
			.fetch();

		long totalCount = queryFactory.selectFrom(product)
			.where(product.name.containsIgnoreCase(keyword))
			.fetchCount();

		return new PageImpl<>(products, pageable, totalCount);
	}
}
