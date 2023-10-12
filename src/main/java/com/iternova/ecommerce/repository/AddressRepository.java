package com.iternova.ecommerce.repository;

import com.iternova.ecommerce.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
}
