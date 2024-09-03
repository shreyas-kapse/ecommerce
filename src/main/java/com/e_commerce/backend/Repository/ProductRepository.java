package com.e_commerce.backend.Repository;

import com.e_commerce.backend.enity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<List<ProductEntity>> findAllByMerchantId(Long id);

    Optional<List<ProductEntity>> findAllByBrand(String companyName);
}
