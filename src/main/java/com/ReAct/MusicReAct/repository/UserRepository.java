package com.ReAct.MusicReAct.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ReAct.MusicReAct.model.User;

import java.util.Set;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    User getById(int id);

    @Query("select u from User u where u.name = ?1")
    Set<User> findAll(String name);
/*
    @Query("SELECT u FROM User u ORDER BY u.id ASC")
    Set<User> getPagination(Pageable pageable);*/
}