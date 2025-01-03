package com.sparta.nanglangeats.domain.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.nanglangeats.domain.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
	Optional<Product> findByUuid(String uuid);
	List<Product> findByStoreIdAndIsPublicTrue(Long storeId);
}
