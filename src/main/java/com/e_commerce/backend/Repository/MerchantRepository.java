package com.e_commerce.backend.Repository;

import com.e_commerce.backend.enity.MerchantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<MerchantEntity,Long> {
    Optional<MerchantEntity> findByMerchantPhoneNo(Long merchantPhoneNo);
    Optional<MerchantEntity> findByEmail(String email);

    Optional<MerchantEntity> findByCompanyName(String companyName);
}