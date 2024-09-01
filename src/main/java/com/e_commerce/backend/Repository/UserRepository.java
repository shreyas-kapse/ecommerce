package com.e_commerce.backend.Repository;

import com.e_commerce.backend.enity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    UserEntity findByUsername(String username);

    Optional<UserEntity> findUserByUsername(String userName);
}
