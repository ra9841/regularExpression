package com.rabin.practice.project.restApi.intel.project.repository;

import com.rabin.practice.project.restApi.intel.project.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity,Long> {
    Optional<CustomerEntity> findByUsername(String username);
}
