package com.sparta.nanglangeats.domain.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.nanglangeats.domain.store.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
