package com.elrealo.firstProject.repository;

import com.elrealo.firstProject.model.AdminStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.elrealo.firstProject.model.User;

@Repository("adminStyleRepository")
public interface AdminStyleRepository extends JpaRepository<AdminStyle, Integer> {
    AdminStyle getFirstByUserId(Integer a);
}