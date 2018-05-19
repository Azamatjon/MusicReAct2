package com.ReAct.MusicReAct.service;

import java.util.Arrays;
import java.util.HashSet;

import com.ReAct.MusicReAct.model.VerificationToken;
import com.ReAct.MusicReAct.repository.VerificationTokenRepository;
import com.ReAct.MusicReAct.repository.RoleRepository;
import com.ReAct.MusicReAct.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ReAct.MusicReAct.model.Role;
import com.ReAct.MusicReAct.model.User;

@Service("userService")
public class UserServiceImpl implements UserService{

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;


    @Qualifier("roleRepository")
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByRole("ADMIN");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        userRepository.save(user);
    }

    @Override
    public User getUser(String verificationToken) {
        User user = tokenRepository.findByToken(verificationToken).getUser();
        return user;
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken();
        myToken.setUser(user);
        myToken.setToken(token);
        myToken.setExpiryDate(1440);
        tokenRepository.save(myToken);
    }

    @Override
    public void saveRegisteredUser(User user) {

        userRepository.save(user);
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }


}