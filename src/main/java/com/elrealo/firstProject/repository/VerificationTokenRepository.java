package com.elrealo.firstProject.repository;

import com.elrealo.firstProject.model.User;
import com.elrealo.firstProject.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository
        extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);
    VerificationToken findByUser(User user);
    VerificationToken findByUserAndAndType(User user, int type);

}