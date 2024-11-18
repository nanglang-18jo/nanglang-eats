package com.sparta.nanglangeats.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sparta.nanglangeats.domain.user.entity.DeliveryAddress;
import com.sparta.nanglangeats.domain.user.entity.User;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {
	@Query("SELECT da FROM DeliveryAddress da JOIN FETCH da.commonAddress WHERE da.user = :user")
	List<DeliveryAddress> findAllByUser(User user);

	@Query("SELECT da FROM DeliveryAddress da JOIN FETCH da.commonAddress WHERE da.id = :id")
	Optional<DeliveryAddress> findByUserWithCommonAddress(Long id);

	Optional<DeliveryAddress> findByUserAndIsRecentDeliveryTrue(User user);
}
