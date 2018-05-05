package com.elrealo.firstProject.controller;

import com.elrealo.firstProject.model.AdminStyle;
import com.elrealo.firstProject.model.User;
import com.elrealo.firstProject.repository.AdminStyleRepository;
import com.elrealo.firstProject.repository.UserRepository;
import com.elrealo.firstProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
public class AjaxController {

    @Autowired
    private UserService userService;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Qualifier("adminStyleRepository")
    @Autowired
    private AdminStyleRepository adminStyleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @RequestMapping(value = "/ajax/lock", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> lock(@RequestParam("type") String type, @RequestParam(value = "pass", required = false) String password) {

        boolean isChanged = true;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        switch (type){
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