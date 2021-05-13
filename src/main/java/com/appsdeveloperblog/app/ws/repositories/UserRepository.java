package com.appsdeveloperblog.app.ws.repositories;

import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, String> {
  UserEntity findByEmail(String email);
  UserEntity findUserById(String userId);
  UserEntity findUserByEmailVerificationToken(String token);
}
