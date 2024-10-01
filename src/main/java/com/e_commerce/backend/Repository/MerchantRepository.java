package com.e_commerce.backend.Repository;

import com.e_commerce.backend.dtos.AllOrdersOfMerchant;
import com.e_commerce.backend.enity.MerchantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<MerchantEntity,Long> {
    Optional<MerchantEntity> findByMerchantPhoneNo(Long merchantPhoneNo);
    Optional<MerchantEntity> findByEmail(String email);

    Optional<MerchantEntity> findByCompanyName(String companyName);

    @Query(name = "get_all_orders_of_merchant_by_merchant_id", nativeQuery = true)
    Optional<List<AllOrdersOfMerchant>> findAllByMerchantId(@Param("merchantId")Long merchantId);
}