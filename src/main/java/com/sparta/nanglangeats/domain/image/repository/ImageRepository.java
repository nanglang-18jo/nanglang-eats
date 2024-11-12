package com.sparta.nanglangeats.domain.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.nanglangeats.domain.image.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
