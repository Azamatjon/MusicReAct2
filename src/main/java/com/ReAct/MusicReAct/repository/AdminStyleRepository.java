package com.ReAct.MusicReAct.repository;

import com.ReAct.MusicReAct.model.AdminStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("adminStyleRepository")
public interface AdminStyleRepository extends JpaRepository<AdminStyle, Integer> {
    AdminStyle getFirstByUserId(Integer a);
}