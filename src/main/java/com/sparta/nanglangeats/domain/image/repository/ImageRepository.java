package com.sparta.nanglangeats.domain.image.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sparta.nanglangeats.domain.image.entity.Image;
import com.sparta.nanglangeats.domain.image.enums.ImageCategory;

import io.lettuce.core.dynamic.annotation.Param;

public interface ImageRepository extends JpaRepository<Image, Long> {
	List<Image> findAllByImageCategoryAndContentId(ImageCategory imageCategory, Long contentId);

	@Query("SELECT i.url FROM Image i WHERE i.imageCategory = :imageCategory AND i.contentId = :contentId")
	List<String> findUrlsByImageCategoryAndContentId(@Param("imageCategory") ImageCategory imageCategory, @Param("contentId") Long contentId);

	List<Image> findByContentIdInAndImageCategory(List<Long> contentId, ImageCategory imageCategory);
}
