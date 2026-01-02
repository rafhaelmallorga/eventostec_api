package com.eventostec.api.repositories;

import com.eventostec.api.domain.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddessRepository extends JpaRepository<Address, UUID> {
}
