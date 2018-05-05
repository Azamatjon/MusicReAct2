package com.elrealo.firstProject.controller;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.Null;

import com.elrealo.firstProject.model.AdminStyle;
import com.elrealo.firstProject.repository.AdminStyleRepository;
import com.elrealo.firstProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;



import java.io.IOException;
import java.security.Principal;

import javax.naming.AuthenticationException;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.servlet.ModelAndView;


import com.elrealo.firstProject.model.User;
import com.elrealo.firstProject.service.UserService;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Qualifier("adminStyleRepository")
    @Autowired
    private AdminStyleRepository adminStyleRepository, adminStyleRepository_;

    private RequestCache requestCache = new HttpSessionRequestCache();
























    /**
     * Demonstrates that {@link HttpServletRequest#authenticate(HttpServletResponse)} will send the user to the log in
     * page configured within Spring Security if the user is not already authenticated.
     */
    @RequestMapping("/authenticate")
    public String authenticate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authenticate = request.authenticate(response);
        return authenticate ? "index" : null;
    }

    /**
     * Authentication with Spring Security using {@link HttpServletRequest#login(String, String)}.
     *
     * <p>
     * If we fail to authenticate, a {@link ServletException} is thrown that wraps the original
     * {@link AuthenticationException} from Spring Security. This means we can catch the {@link ServletException} to
     * display the error message. Alternatively, we could allow the {@link ServletException} to propagate and Spring
     * Security's {@link ExceptionTranslationFilter} would catch it and process it appropriately.
     * </p>
     *
     */
    @RequestMapping(value = "/ajax/login", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> login(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestParam(value = "email", required = false) String email,
                                     @RequestParam(value = "password", required = false) String password
                        ) throws ServletException {
        HashMap<String, String> map = new HashMap<>();

        try {
            request.login(email, password);
            SavedRequest savedRequest = requestCache.getRequest(request, response);

            if (savedRequest != null) {
                map.put("status", "success");
            } else {
                map.put("status", "success2");
            }

        } catch (ServletException authenticationFailed) {
            map.put("status", "failed");
        }
        return map;
    }

    
    /**
     * Demonstrates that invoking {@link HttpServletRequest#logout()} will log the user out.
     * We then redirect the user to the home page.
     */
    /*
    @RequestMapping("/logout2")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "redirect:/";
    }
    */















    /* Ajax */
    @RequestMapping(value = "/ajaxtest", method = RequestMethod.GET)
    @ResponseBody
    public Set<String> ajaxTest() {
        Set<String> records = new HashSet<String>();
        records.add("Record #1");
        records.add("Record #2");

        return records;
    }
/*
    @RequestMapping(value={"/"}, method = RequestMethod.GET)
    public ModelAndView homePage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }*/

    @RequestMapping(value={"/login"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/login");
        return modelAndView;
    }

    @RequestMapping("/galleryScroll")
    public ModelAndView scroll(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("galleryScroll.html");
        return modelAndView;
    }




    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }

    @RequestMapping(value="/admin/home", method = RequestMethod.GET)
    public ModelAndView home(@RequestParam(value = "action", required = false) String action) {

        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        if (user == null) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }


        if (user.isLocked()) {
            modelAndView.addObject("user", user);
            modelAndView.addObject("avatar", "/avatars/" + user.getImage_name());
            modelAndView.setViewName("admin/locked");

        } else {
            AdminStyle style = adminStyleRepository.getFirstByUserId(user.getId());
            String styles = "";

            if (style == null) {
                style = new AdminStyle();

                style.setUserId(user.getId());
                style.setCustomStyle("white");
                style.setfHeader(true);
                style.setfSidebar(false);
                style.sethBar(false);
                style.settSidebar(false);
                style.setcMenu(false);
                style.sethMenu(false);
                style.setbLayout(false);

                adminStyleRepository_.save(style);

                System.out.println("Style was initialized.");
            }
            System.out.println("fheader: " + style.isfHeader());
            // Styles
            modelAndView.addObject("themeCSS", "/adm/assets/css/themes/" + style.getCustomStyle() + ".css");

            if (style.isfHeader()) {
                styles += " page-header-fixed";
                modelAndView.addObject("fHeader", "checked");
            } else modelAndView.addObject("fHeader", "");

            if (style.isfSidebar()) {
                styles += " page-sidebar-fixed";
                modelAndView.addObject("fSidebar", "checked");
            } else modelAndView.addObject("fSidebar", "");

            if (style.ishBar()) {
                styles += " page-horizontal-bar";
                modelAndView.addObject("hBar", "checked");
                modelAndView.addObject("hBar_", "horizontal-bar");
            } else {
                modelAndView.addObject("hBar", "");
                modelAndView.addObject("hBar_", "page-sidebar");
            }

            if (style.istSidebar()) {
                styles += " small-sidebar";
                modelAndView.addObject("tSidebar", "checked");
            } else modelAndView.addObject("tSidebar", "");

            if (style.iscMenu()) {
                styles += " compact-menu";
                modelAndView.addObject("cMenu", "checked");
            } else modelAndView.addObject("cMenu", "");

            if (style.ishMenu()) {
                styles += " hover-menu";
                modelAndView.addObject("hMenu", "checked");
            } else modelAndView.addObject("hMenu", "");

            if (style.isbLayout()) {
                modelAndView.addObject("bLayout", "checked");
                modelAndView.addObject("bLayout_", "container");
            } else {
                modelAndView.addObject("bLayout", "");
                modelAndView.addObject("bLayout_", "");
            }

            modelAndView.addObject("styles", styles);
            modelAndView.addObject("style", style);


            modelAndView.addObject("user", user);
            modelAndView.addObject("avatar", "/avatars/" + user.getImage_name());

            modelAndView.setViewName("admin/default");

            if (action == null || action.equals("dashboard")) {
                modelAndView.addObject("content", "dashboard");
            } else if (action.equals("profile")) {
                modelAndView.addObject("content", "profile");
            }
        }
        return modelAndView;
    }


}