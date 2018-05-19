package com.ReAct.MusicReAct.service;

import com.ReAct.MusicReAct.model.VerificationToken;
import com.ReAct.MusicReAct.model.User;

public interface UserService {
    public User findUserByEmail(String email);
    public void saveUser(User user);

    public void saveRegisteredUser(User user);
    public User getUser(String verificationToken);
    public void createVerificationToken(User user, String token);
    public VerificationToken getVerificationToken(String VerificationToken);
}