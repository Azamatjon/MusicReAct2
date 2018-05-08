package com.elrealo.firstProject.controller;

import com.elrealo.firstProject.Helper.RandomString;
import com.elrealo.firstProject.model.AdminStyle;
import com.elrealo.firstProject.model.Role;
import com.elrealo.firstProject.model.User;
import com.elrealo.firstProject.model.VerificationToken;
import com.elrealo.firstProject.repository.AdminStyleRepository;
import com.elrealo.firstProject.repository.RoleRepository;
import com.elrealo.firstProject.repository.UserRepository;
import com.elrealo.firstProject.repository.VerificationTokenRepository;
import com.elrealo.firstProject.service.MailClient;
import com.elrealo.firstProject.service.StorageService;
import com.elrealo.firstProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;
import java.util.Arrays;

@Controller
public class AjaxController {

    @Autowired
    private UserService userService;

    @Autowired
    StorageService storageService;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Qualifier("adminStyleRepository")
    @Autowired
    private AdminStyleRepository adminStyleRepository;

    @Qualifier("roleRepository")
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;


    @Autowired
    private MailClient mailClient;



    private RequestCache requestCache = new HttpSessionRequestCache();

    @RequestMapping(value = "/ajax/lock", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> lock(@RequestParam("action") String action, @RequestParam(value = "pass", required = false) String password) {

        boolean isChanged = true;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        switch (action){
            case "lock":
                user.setLocked(true);
                break;
            case "unlock":
                if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
                    user.setLocked(false);
                } else {
                    isChanged = false;
                }
                break;

            default:
                isChanged = false;
                break;
        }



        HashMap<String, String> map = new HashMap<>();

        if (isChanged){
            userRepository.save(user);
            map.put("status", "ok");
        } else {
            map.put("status", "incorrect");
        }
        return map;
    }

    @RequestMapping(value = "/ajax/user", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> userAPI(@RequestParam("action") String action,
                                       @RequestParam(value = "userId", required = false) Integer userId,
                                       @RequestParam(value = "active", required = false) Integer active,
                                       @RequestParam(value = "email", required = false) String email,
                                       @RequestParam(value = "firstName", required = false) String firstName,
                                       @RequestParam(value = "lastName", required = false) String lastName,
                                       @RequestParam(value = "oldPassword", required = false) String oldPassword,
                                       @RequestParam(value = "newPassword", required = false) String newPassword,
                                       @RequestParam(value = "password", required = false) String password,
                                       @RequestParam(value = "roleId", required = false) Integer roleId,
                                       @RequestParam(value = "avatar", required = false) MultipartFile avatar,
                                       HttpServletRequest request,
                                       HttpServletResponse response
                                       ) {
        HashMap<String, String> map = new HashMap<>();
        boolean isChanged = true;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        switch (action){
            case "deleteAvatar":
                if (userId != null){
                    if (userId != null){
                        User changingUser = userRepository.findById((int)userId);
                        changingUser.setImage(null);
                        userRepository.save(changingUser);

                        map.put("status", "success");
                        map.put("avatar", "/avatars/default_avatar.png");
                    } else {
                        map.put("status", "error");
                        map.put("error_code", "You don't have change data privilegies, to another users.");
                    }
                } else {
                    user.setImage(null);
                    userRepository.save(user);

                    map.put("status", "success");
                    map.put("avatar", "/avatars/default_avatar.png");
                }
                break;
            case "edit":
                if (userId != null){
                    if (user.isAdmin()){
                        User changingUser = userRepository.findById((int)userId);

                        System.out.println("You are changing: " + changingUser.getEmail());

                        if (!email.isEmpty() && email != null){
                            if (!changingUser.getEmail().equals(email.toLowerCase())){
                                if (userRepository.findByEmail(email) == null){
                                    changingUser.setEmail(email);
                                } else {
                                    map.put("status", "error");
                                    map.put("error_code", "Entered email (" + email + "), already registered by another user. " );
                                }
                            }
                        }

                        if (!firstName.isEmpty() && firstName != null){
                            changingUser.setName(firstName);
                        }

                        if (!lastName.isEmpty() && lastName != null){
                            changingUser.setLastName(lastName);
                        }

                        if (avatar != null){
                            String generatedAvatarName = storageService.storeAvatar(avatar);
                            changingUser.setImage(generatedAvatarName);
                        }

                        userRepository.save(changingUser);
                        if (!map.containsKey("status")) map.put("status", "success");

                        map.put("avatar", "/avatars/" + ((changingUser.getImage() == null)?"default_avatar.png":changingUser.getImage()));

                        map.put("firstName", changingUser.getName());
                        map.put("lastName", changingUser.getLastName());
                        map.put("email", changingUser.getEmail());

                    } else {
                        map.put("status", "error");
                        map.put("error_code", "You don't have change data privilegies, to another users.");
                    }
                } else {
                    if (!email.isEmpty() && email != null){
                        if (!user.getEmail().equals(email.toLowerCase())){

                            if (userRepository.findByEmail(email) == null){
                                VerificationToken previousToken = verificationTokenRepository.findByUserAndAndType(user, 2);
                                if (previousToken != null){
                                    verificationTokenRepository.delete(previousToken);
                                }
                                VerificationToken token = new VerificationToken();
                                token.setUser(user);
                                token.setEmail(email);
                                token.setType(2);
                                token.setExpiryDate(1440);
                                token.generateToken();

                                verificationTokenRepository.save(token);

                                mailClient.prepareAndSend(email, "Confirmation Changing Email", "Please confirm via http://localhost:8080/confirmChangingEmail?token=" + token.getToken());
                                map.put("status", "warning");
                                map.put("warning_code", "All your data changed except your email, we sent you (" + email.toLowerCase() + ") an email, please verify your email." );
                            } else {
                                map.put("status", "error");
                                map.put("error_code", "Entered email (" + email + "), already registered by another user. " );
                            }


                        }
                    }

                    if (!firstName.isEmpty() && firstName != null){
                        user.setName(firstName);
                    }

                    if (!lastName.isEmpty() && lastName != null){
                        user.setLastName(lastName);
                    }


                    if (avatar != null){
                        String generatedAvatarName = storageService.storeAvatar(avatar);
                        user.setImage(generatedAvatarName);
                    }

                    userRepository.save(user);

                    if (!map.containsKey("status")) map.put("status", "success");

                    map.put("avatar", "/avatars/" + ((user.getImage() == null)?"default_avatar.png":user.getImage()));
                    map.put("firstName", user.getName());
                    map.put("lastName", user.getLastName());
                    map.put("email", user.getEmail());
                }
                break;
            case "changePassword":
                if (userId != null){
                    if (user.isAdmin()){
                        User changingPasswordUser = userRepository.findById((int)userId);
                        changingPasswordUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
                        userRepository.save(changingPasswordUser);
                        map.put("status", "success");
                    } else {
                        map.put("status", "error");
                        map.put("error_code", "You don't have change password privilegies, to another users.");
                    }
                } else if (bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
                    user.setPassword(bCryptPasswordEncoder.encode(newPassword));
                    userRepository.save(user);
/*
                    try {
                        request.logout();
                        requestCache.saveRequest(request, response);


                        request.login(user.getEmail(), newPassword);
                        requestCache.saveRequest(request, response);

                        SavedRequest savedRequest = requestCache.getRequest(request, response);

                        if (savedRequest != null) {
                            map.put("status2", "success");
                        } else {
                            map.put("status2", "success");
                        }
                        map.put("redirect", request.getHeader("referer"));

                       } catch (ServletException e) {
                        map.put("status2", "error");
                        System.out.println("Authorization error: " + e.getMessage());
                    }*/

                    map.put("status", "success");
                } else {
                    map.put("status", "error");
                    map.put("error_code", "Old password is incorrect.");
                }
                break;

            case "add":
                if (user.isAdmin()){

                    if (!email.isEmpty() && email != null){
                        if (userRepository.findByEmail(email) == null){
                            User newUser = new User();
                            newUser.setEmail(email);
                            newUser.setName(firstName);
                            newUser.setLastName(lastName);
                            newUser.setActive((int)active);
                            newUser.setLocked(false);

                            Role selectedRole = roleRepository.findById((int)roleId);
                            Set<Role> setRole = new HashSet<Role>();

                            if (selectedRole != null){
                                setRole.add(selectedRole);
                            }

                            newUser.setRoles(setRole);
                            newUser.setPassword(bCryptPasswordEncoder.encode(password));

                            userRepository.save(newUser);

                            map.put("status", "success");
                            map.put("redirect", "/admin/home?goTo=users");
                        } else {
                            map.put("status", "error");
                            map.put("error_code", "Entered email (" + email + "), already registered by another user. " );
                        }
                    }
                } else {
                    map.put("status", "error");
                    map.put("error_code", "You don't have enough permissions.");
                }
                break;

            case "delete":
                if (userId != null){
                    if (user.isAdmin()){
                        User userToDelete = userRepository.findById((int)userId);
                        if (userToDelete != null){
                            userRepository.delete(userToDelete);
                            map.put("status", "success");
                        } else {
                            map.put("status", "error");
                            map.put("error_code", "Entered email (" + email + "), already registered by another user. " );
                        }

                    } else {
                        map.put("status", "error");
                        map.put("error_code", "You don't have enough permissions.");
                    }
                } else if (bCryptPasswordEncoder.matches(password, user.getPassword())) {

                    userRepository.delete(user);

                    try {
                        request.logout();
                        requestCache.saveRequest(request, response);
                    } catch (ServletException e) {
                        System.out.println("Logout error: " + e.getMessage());
                    }

                    map.put("status", "success");
                    map.put("redirect", "/logout");
                } else {
                    map.put("status", "error");
                    map.put("error_code", "Entered password incorrect.");
                }

                break;

            default:
                map.put("status", "error");
                map.put("error_code", "No such action defined.");

                break;
        }

        return map;
    }




    /* Ajax */
    @RequestMapping(value = "/ajax/changeStyle", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> changeStyle(@RequestParam("type") String type, @RequestParam("value") String value) {

        boolean isChanged = true;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        AdminStyle style = adminStyleRepository.getFirstByUserId(user.getId());
        boolean value_ = (value.equals("true")?true:false);
        switch (type){
            case "f_header":
                style.setfHeader(value_);
                break;
            case "f_sidebar":
                style.setfSidebar(value_);
                break;
            case "h_bar":
                style.sethBar(value_);
                break;
            case "t_sidebar":
                style.settSidebar(value_);
                break;
            case "c_menu":
                style.setcMenu(value_);
                break;
            case "h_menu":
                style.sethMenu(value_);
                break;
            case "b_layout":
                style.setbLayout(value_);
                break;
            case "custom_style":
                style.setCustomStyle(value);
                break;
            case "reset":
                style.setCustomStyle("white");
                style.setfHeader(true);
                style.setfSidebar(false);
                style.sethBar(false);
                style.settSidebar(false);
                style.setcMenu(false);
                style.sethMenu(false);
                style.setbLayout(false);

                break;
            default:
                isChanged = false;
                break;
        }

        HashMap<String, String> map = new HashMap<>();

        if (isChanged){
            adminStyleRepository.save(style);
            map.put("status", "ok");
        } else {
            map.put("status", "error");
        }
        return map;
    }


}