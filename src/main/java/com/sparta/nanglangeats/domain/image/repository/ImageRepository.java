package com.sparta.nanglangeats.domain.image.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.nanglangeats.domain.image.entity.Image;
import com.sparta.nanglangeats.domain.image.enums.ImageCategory;

public interface ImageRepository extends JpaRepository<Image, Long> {
	List<Image> findAllByImageCategoryAndAndContentId(ImageCategory imageCategory, Long contentId);

	List<Image> findByContentIdInAndImageCategory(List<Long> contentId, ImageCategory imageCategory);
}
