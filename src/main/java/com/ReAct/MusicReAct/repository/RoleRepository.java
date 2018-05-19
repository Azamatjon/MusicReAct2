package com.ReAct.MusicReAct.repository;

import com.ReAct.MusicReAct.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository("roleRepository")
public interface RoleRepository extends JpaRepository<Role, Integer>{
    Role findByRole(String role);
    Role findById(int id);

    @Query("select r from Role r")
    Set<Role> getAll();

}