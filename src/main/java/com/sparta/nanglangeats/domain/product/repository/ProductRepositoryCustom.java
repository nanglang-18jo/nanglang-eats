package com.sparta.nanglangeats.domain.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sparta.nanglangeats.domain.product.entity.Product;

public interface ProductRepositoryCustom {
	Page<Product> searchProductByKeyword(String keyword, Pageable pageable);
}
