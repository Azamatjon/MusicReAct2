package com.elrealo.firstProject.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.elrealo.firstProject.model.User;

import java.util.List;
import java.util.Set;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    User findById(int id);

    @Query("select u from User u where u.name = ?1")
    Set<User> findAll(String name);
/*
    @Query("SELECT u FROM User u ORDER BY u.id ASC")
    Set<User> getPagination(Pageable pageable);*/
}