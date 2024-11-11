package com.sparta.nanglangeats.domain.address.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.nanglangeats.domain.address.entity.CommonAddress;

public interface CommonAddressRepository extends JpaRepository<CommonAddress, Long> {
	Optional<CommonAddress> findByAddress(String address);
}
