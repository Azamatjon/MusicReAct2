package com.ReAct.MusicReAct.repository;

import com.ReAct.MusicReAct.model.User;
import com.ReAct.MusicReAct.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository
        extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);
    VerificationToken findByUser(User user);
    VerificationToken findByUserAndAndType(User user, int type);

}