package com.sparta.nanglangeats.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.nanglangeats.domain.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
