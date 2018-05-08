package com.elrealo.firstProject.service;

import com.elrealo.firstProject.model.User;
import com.elrealo.firstProject.model.VerificationToken;

public interface UserService {
    public User findUserByEmail(String email);
    public void saveUser(User user);

    public void saveRegisteredUser(User user);
    public User getUser(String verificationToken);
    public void createVerificationToken(User user, String token);
    public VerificationToken getVerificationToken(String VerificationToken);
}