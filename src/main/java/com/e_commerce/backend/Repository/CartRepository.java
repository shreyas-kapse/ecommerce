package com.e_commerce.backend.Repository;

import com.e_commerce.backend.enity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity,Long> {
    Optional<CartEntity> findByUserId(Long userIdObject);
}
