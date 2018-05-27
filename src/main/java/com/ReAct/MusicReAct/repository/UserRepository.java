package com.ReAct.MusicReAct.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ReAct.MusicReAct.model.User;

import java.util.List;
import java.util.Set;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    User getById(int id);

    @Query("select u from User u where u.name = ?1")
    Set<User> findAll(String name);


    @Query("select u from User u where u.name like %?1% OR u.lastName like %?1%")
    Page<User> liteSearch(String query, Pageable pageable);

    List<User> findTop3ByOrderByIdDesc();
}