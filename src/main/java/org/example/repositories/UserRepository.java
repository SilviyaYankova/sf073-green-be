package org.example.repositories;

import org.example.models.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsernameIgnoreCase(String userName);

    @Query("select u from UserEntity as u order by u.id")
    List<UserEntity> findAll();

    long count();
}
