package com.elrealo.firstProject.service;

import com.elrealo.firstProject.model.User;

public interface UserService {
    public User findUserByEmail(String email);
    public void saveUser(User user);
}