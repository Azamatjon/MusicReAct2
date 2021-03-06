package com.ReAct.MusicReAct.controller;

import com.ReAct.MusicReAct.Helper.CssSwitcher;
import com.ReAct.MusicReAct.Helper.Pagination;
import com.ReAct.MusicReAct.model.*;
import com.ReAct.MusicReAct.repository.*;
import com.ReAct.MusicReAct.service.MailClient;
import com.ReAct.MusicReAct.service.StorageService;
import com.ReAct.MusicReAct.service.UserService;
import com.ReAct.MusicReAct.service.UserServiceImpl;
import com.ReAct.MusicReAct.Helper.PaginationPage;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;


import java.io.IOException;

import javax.naming.AuthenticationException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;



    @Qualifier("roleRepository")
    @Autowired
    private RoleRepository roleRepository;

    @Qualifier("adminStyleRepository")
    @Autowired
    private AdminStyleRepository adminStyleRepository;


    @Autowired
    private MailClient mailClient;

    @Qualifier("albumRepository")
    @Autowired
    private AlbumRepository albumRepository;

    @Qualifier("trackRepository")
    @Autowired
    private TrackRepository trackRepository;


    @Qualifier("artistRepository")
    @Autowired
    private ArtistRepository artistRepository;


    @Qualifier("galleryRepository")
    @Autowired
    private GalleryRepository galleryRepository;

    private RequestCache requestCache = new HttpSessionRequestCache();

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private StorageService storageService;


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    String employeesPageable() {

        Pageable top = new PageRequest(0, 6);

        Page<Artist> artists = artistRepository.tops(top);

        for (Artist a:artists.getContent()) {
            System.out.println("Artist name: " + a.getName());
        }

        return "test";
    }











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

            System.out.printf("email: %s, password: %s\n", email, password);
            request.logout();

            request.login(email, password);

            //System.out.println(request.getUserPrincipal().getName());
            requestCache.saveRequest(request, response);
            SavedRequest savedRequest = requestCache.getRequest(request, response);
            //System.out.println("" + request.isUserInRole("ADMIN"));
            if (savedRequest != null) {
                map.put("status", "success");
            } else {
                map.put("status", "success2");
            }
            User user = userService.findUserByEmail(request.getUserPrincipal().getName());
            if (user.isAdmin()){
                map.put("redirect", "/admin/home");
            } else {
                map.put("redirect", "/");
            }


        } catch (ServletException authenticationFailed) {
            map.put("status", "failed");
            map.put("error_code", authenticationFailed.getMessage());
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
    @RequestMapping(value = "/email", method = RequestMethod.GET)
    @ResponseBody
    public Set<String> ajaxTest() {
        Set<String> records = new HashSet<String>();
        records.add("sent");


        return records;
    }


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
        modelAndView.setViewName("admin/register");
        return modelAndView;
    }


    @RequestMapping(value="/confirmRegistration", method = RequestMethod.GET)
    public ModelAndView confirmationRegistration(@RequestParam(value = "token", required = false) String token){

        ModelAndView modelAndView = new ModelAndView();

        VerificationToken checkToken = verificationTokenRepository.findByToken(token);

        try {
            if (checkToken.getUser().getActive() != 1 && checkToken.getType() == 1) {
                modelAndView.addObject("token", token);
                modelAndView.addObject("email", checkToken.getUser().getEmail());
                modelAndView.setViewName("admin/confirmRegistration");
            } else if (checkToken.getType() != 1) {
                throw new NullPointerException();
            }  else {
                modelAndView.addObject("error_code", "403");
                modelAndView.addObject("error_context", "Oops! Something went wrong");
                modelAndView.addObject("error_message", "This email is already confirmed.");
                modelAndView.setViewName("admin/errorPage");
            }
        } catch (NullPointerException e){
            modelAndView.addObject("error_code", "403");
            modelAndView.addObject("error_context", "Oops! Something went wrong");
            modelAndView.addObject("error_message", "This token is not valid.");
            modelAndView.setViewName("admin/errorPage");
        }
        return modelAndView;
    }

    @RequestMapping(value="/ajax/confirmRegistration", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> confirmationRegistrationLastStep(@RequestParam(value = "token", required = false) String token,
                                                @RequestParam(value = "firstName", required = false) String firstName,
                                                @RequestParam(value = "lastName", required = false) String lastName,
                                                @RequestParam(value = "password", required = false) String password){

        HashMap<String, String> map = new HashMap<>();

        VerificationToken checkToken = verificationTokenRepository.findByToken(token);

        try {
            if (checkToken.getUser().getActive() != 1 && checkToken.getType() == 1){
                User user = userRepository.findByEmail(checkToken.getUser().getEmail());
                user.setActive(1);
                user.setName(firstName);
                user.setLastName(lastName);
                user.setPassword(bCryptPasswordEncoder.encode(password));

                userRepository.save(user);
                verificationTokenRepository.delete(checkToken);
                map.put("status", "success");
            } else if (checkToken.getType() != 1) {
                map.put("status", "error");
                map.put("error_description", "This token is not valid.");
            } else {
                map.put("status", "error");
                map.put("error_description", "This email is already confirmed.");
            }
        } catch (NullPointerException e){
            map.put("status", "error");
            map.put("error_description", "This token is not valid / or: " + e.getMessage());
        }
        return map;
    }





    @RequestMapping(value="/confirmChangingEmail", method = RequestMethod.GET)
    public ModelAndView confirmChangingEmail(@RequestParam(value = "token", required = false) String token){

        ModelAndView modelAndView = new ModelAndView();

        VerificationToken checkToken = verificationTokenRepository.findByToken(token);

        try {
            if (checkToken.getType() == 2) {
                modelAndView.addObject("token", token);
                modelAndView.addObject("oldEmail", checkToken.getUser().getEmail());
                modelAndView.addObject("newEmail", checkToken.getEmail());
                modelAndView.setViewName("admin/confirmChangingEmail");
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e){
            modelAndView.addObject("error_code", "403");
            modelAndView.addObject("error_context", "Oops! Something went wrong");
            modelAndView.addObject("error_message", "This token is not valid.");
            modelAndView.setViewName("admin/errorPage");
        }
        return modelAndView;
    }

    @RequestMapping(value="/confirmChangingEmail", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> confirmChangingEmailLastStep(@RequestParam(value = "token", required = false) String token,
                                                            @RequestParam(value = "newEmail", required = false) String newEmail,
                                                            @RequestParam(value = "password", required = false) String password,
                                                            HttpServletRequest request,
                                                            HttpServletResponse response){

        HashMap<String, String> map = new HashMap<>();

        VerificationToken checkToken = verificationTokenRepository.findByToken(token);
        boolean isPasswordValid = bCryptPasswordEncoder.matches(password, checkToken.getUser().getPassword());
        try {
            if (isPasswordValid && checkToken.getType() == 2){
                User user = checkToken.getUser();
                user.setEmail(newEmail);

                userRepository.save(user);

                verificationTokenRepository.delete(checkToken);

                request.logout();
                request.login(user.getEmail(), password);

                requestCache.saveRequest(request, response);
                SavedRequest savedRequest = requestCache.getRequest(request, response);

                if (savedRequest != null) {
                    map.put("status2", "success");
                } else {
                    map.put("status2", "success2");
                }

                if (user.isAdmin()){
                    map.put("redirect", "/admin/home?goTo=profile");
                } else {
                    map.put("redirect", "/admin/home?goTo=profile");
                }


                map.put("status", "success");
            } else if (!isPasswordValid){
                map.put("status", "error");
                map.put("error_description", "Password you've entered is not valid!");
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e){
            map.put("status", "error");
            map.put("error_description", "This token is not valid / or: " + e.getMessage());
        } catch (ServletException e){
            map.put("status", "error");
            map.put("error_description", "Error occured while logging in / or: " + e.getMessage());
        }
        return map;
    }

    @RequestMapping(value="/forgotPassword", method = RequestMethod.GET)
    public ModelAndView forgotPassword() {

        ModelAndView modelAndView = new ModelAndView();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        if (user == null) {
            modelAndView.setViewName("admin/forgotPassword");
        } else {
            modelAndView.addObject("error_code", "403");
            modelAndView.addObject("error_context", "Oops! Something went wrong");
            modelAndView.addObject("error_message", "You have alreay logged in.");
            modelAndView.setViewName("admin/errorPage");
        }
        return modelAndView;
    }


    @RequestMapping(value="/confirmForgotPassword", method = RequestMethod.GET)
    public ModelAndView confirmForgotPassword(@RequestParam(value = "token", required = false) String token){

        ModelAndView modelAndView = new ModelAndView();

        VerificationToken checkToken = verificationTokenRepository.findByToken(token);

        try {
            if (checkToken.getType() == 3) {
                modelAndView.addObject("token", token);
                modelAndView.addObject("email", checkToken.getUser().getEmail());
                modelAndView.setViewName("admin/confirmForgotPassword");
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e){
            modelAndView.addObject("error_code", "403");
            modelAndView.addObject("error_context", "Oops! Something went wrong");
            modelAndView.addObject("error_message", "This token is not valid.");
            modelAndView.setViewName("admin/errorPage");
        }
        return modelAndView;
    }

    @RequestMapping(value="/ajax/confirmForgotPassword", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> confirmForgotPasswordLastStep(@RequestParam(value = "token", required = false) String token,
                                                             @RequestParam(value = "newPassword", required = false) String newPassword,
                                                            HttpServletRequest request,
                                                            HttpServletResponse response){

        HashMap<String, String> map = new HashMap<>();

        VerificationToken checkToken = verificationTokenRepository.findByToken(token);

        try {
            if (checkToken.getType() == 3){
                User user = checkToken.getUser();
                user.setPassword(bCryptPasswordEncoder.encode(newPassword));

                userRepository.save(user);

                verificationTokenRepository.delete(checkToken);

                request.logout();
                request.login(user.getEmail(), newPassword);
                requestCache.saveRequest(request, response);
                SavedRequest savedRequest = requestCache.getRequest(request, response);

                if (savedRequest != null) {
                    map.put("status2", "success");
                } else {
                    map.put("status2", "success2");
                }

                if (user.isAdmin()){
                    map.put("redirect", "/admin/home");
                } else {
                    map.put("redirect", "/admin/home");
                }


                map.put("status", "success");
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e){
            map.put("status", "error");
            map.put("error_description", "This token is not valid / or: " + e.getMessage());
        } catch (ServletException e){
            map.put("status", "error");
            map.put("error_description", "Error occured while logging in / or: " + e.getMessage());
        }
        return map;
    }
















    @RequestMapping(value = "/ajax/registration", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> createNewUser(@RequestParam(value = "email", required = false) String email) {

        HashMap<String, String> map = new HashMap<>();

        User userExists = userService.findUserByEmail(email);


        if (userExists != null && userExists.getActive() == 1) {
            map.put("status", "error");
            map.put("error_description", "User exists");
        } else {
            /*try {*/
            try {
                if (userExists != null && userExists.getActive() == 0){
                    VerificationToken prevousToken = verificationTokenRepository.findByUserAndAndType(userExists, 1);
                    if (prevousToken != null){
                        verificationTokenRepository.delete(prevousToken);
                        System.out.println("Previous confirm registration token was deleted");
                    }
                    userRepository.delete(userExists);
                }
            } catch (Exception e){
                System.out.println("isNull: ");
                userRepository.delete(userExists);
            }
            User user = new User();
            user.setEmail(email);
            user.setLocked(false);
            user.setActive(0);
            user.setPassword(bCryptPasswordEncoder.encode("temporary"));

            userService.saveUser(user);

            VerificationToken token = new VerificationToken();
            token.setUser(user);
            token.setType(1);
            token.setExpiryDate(1440);
            token.generateToken();

            verificationTokenRepository.save(token);

            mailClient.prepareAndSend(email, "Confirmation Registration", "Please confirm via http://localhost:8080/confirmRegistration?token=" + token.getToken());

            map.put("status", "success");
            /*} catch (Exception e){
                map.put("status", "error");
                map.put("error_description", e.getMessage());
            }
            */
        }
        return map;
    }














    @RequestMapping(value = "/ajax/restorePassword", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> restorePassword(@RequestParam(value = "email", required = false) String email) {

        HashMap<String, String> map = new HashMap<>();

        User userExists = userService.findUserByEmail(email);


        try{
            if (userExists != null && userExists.getActive() == 1) {
                try {
                    VerificationToken previousToken = verificationTokenRepository.findByUserAndAndType(userExists, 3);
                    if (previousToken != null){
                        verificationTokenRepository.delete(previousToken);
                        System.out.println("previous restore password token was deleted");
                    }
                } catch (Exception e){
                    System.out.println("previous restore password token wasn't deleted");
                }

                VerificationToken token = new VerificationToken();
                token.setUser(userExists);
                token.setEmail(email);
                token.setType(3);
                token.setExpiryDate(1440);
                token.generateToken();

                verificationTokenRepository.save(token);

                mailClient.prepareAndSend(email, "Restore password request", "Please go to http://localhost:8080/confirmForgotPassword?token=" + token.getToken() + " to restore your password.");

                map.put("status", "success");
            } else if (userExists.getActive() == 0){
                map.put("status", "error"); 
                map.put("error_description", "User wasn't activated yet");
            } else {
                map.put("status", "error");
                map.put("error_description", "Some error was occured. ");
            }
        } catch (NullPointerException e){
            map.put("status", "error");
            map.put("error_description", "User doesn't exist. ");
        }
        return map;
    }

    @Autowired
    UserServiceImpl userServiceImpl;


    @RequestMapping(value="/admin/home", method = RequestMethod.GET)
    public ModelAndView home(@RequestParam(value = "action", required = false) String action,
                             @RequestParam(value = "goTo", required = false) String goTo,
                             @RequestParam(value = "id", required = false) Integer id,
                             Pageable pageable) {

        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());


        boolean isErrorFound = false;

        if (user == null) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }


        if (user.isLocked()) {
            modelAndView.addObject("user", user);
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

                adminStyleRepository.save(style);

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

            modelAndView.setViewName("admin/default");

            if (goTo != null) modelAndView.addObject("goTo", goTo);
            else modelAndView.addObject("goTo", "null");

            if (action != null) modelAndView.addObject("action", action);
            else modelAndView.addObject("action", "null");
            if (goTo == null) goTo = "noWhere";
            if (action == null) action = "noAction";

            switch (goTo){
                case "users":
                    switch (action) {
                        case "add":
                            modelAndView.addObject("roleList", roleRepository.getAll());

                            break;

                        case "edit":
                            modelAndView.addObject("roleList", roleRepository.getAll());
                            User eUser = userRepository.getById(id);
                            if (eUser != null){

                                modelAndView.addObject("roleList", roleRepository.getAll());
                                modelAndView.addObject("eUser", eUser);
                                modelAndView.addObject("eUserAvatar", eUser.getImage());

                            } else {
                                isErrorFound = true;
                                modelAndView.addObject("goTo", "");
                                modelAndView.addObject("error_code", "404");
                                modelAndView.addObject("error_context", "Oops ! User not found.");
                                modelAndView.addObject("error_message", "We couldn't find user with id: " + id);
                            }

                            break;

                        case "noAction":

                            if (pageable != null){
                                Page users = userRepository.findAll(pageable);
                                Pagination pagination = new Pagination(users, pageable, 5);

                                modelAndView.addObject("pages", pagination.getPages());
                                modelAndView.addObject("users", users.getContent());
                                modelAndView.addObject("currentPage", pagination.getPageNumber());
                                modelAndView.addObject("pageSize", pagination.getPageSize());
                            }

                            break;
                        default:
                            break;
                    }
                    break;
                case "artists":
                    switch (action) {
                        case "add":
                            break;

                        case "edit":
                            Artist eArtist = artistRepository.getOne(id);
                            if (eArtist != null){

                                modelAndView.addObject("eArtist", eArtist);
                                modelAndView.addObject("eArtistAvatar", "/artistsAvatar/" + ((eArtist.getImage() == null)?"default_avatar.png":eArtist.getImage()));

                            } else {
                                isErrorFound = true;
                                modelAndView.addObject("goTo", "");
                                modelAndView.addObject("error_code", "404");
                                modelAndView.addObject("error_context", "Oops ! Artist not found.");
                                modelAndView.addObject("error_message", "We couldn't find artist with id: " + id);
                            }

                            break;

                        case "noAction":
                            modelAndView.addObject("trackRepository", trackRepository);
                            modelAndView.addObject("albumRepository", albumRepository);
                            if (pageable != null){
                                Page artists = (user.isAdmin()) ? artistRepository.findAll(pageable) : artistRepository.findAllByUser(user, pageable);
                                Pagination pgn = new Pagination(artists, pageable, 5);
                                modelAndView.addObject("pages", pgn.getPages());

                                modelAndView.addObject("currentPage", pgn.getPageNumber());
                                modelAndView.addObject("pageSize", pgn.getPageSize());
                                modelAndView.addObject("artists", artists.getContent());
                            }

                            break;
                        default:
                            break;
                    }
                    break;
                case "albums":
                    switch (action) {
                        case "add":
                            break;

                        case "edit":
                            Album eAlbum = albumRepository.getOne(id);
                            if (eAlbum != null){

                                modelAndView.addObject("eAlbum", eAlbum);
                                modelAndView.addObject("eAlbumAvatar", eAlbum.getImage());

                            } else {
                                isErrorFound = true;
                                modelAndView.addObject("error_code", "404");
                                modelAndView.addObject("error_context", "Oops ! Artist not found.");
                                modelAndView.addObject("error_message", "We couldn't find artist with id: " + id);

                                modelAndView.addObject("goTo", "");

                                }

                            break;

                        case "noAction":
                            modelAndView.addObject("trackRepository", trackRepository);
                            if (pageable != null){
                                Page artists = (user.isAdmin()) ? albumRepository.findAll(pageable) : albumRepository.findAllByUser(user, pageable);
                                Pagination pgn = new Pagination(artists, pageable, 5);

                                modelAndView.addObject("currentPage", pgn.getPageNumber());
                                modelAndView.addObject("pageSize", pgn.getPageSize());
                                modelAndView.addObject("albums", artists.getContent());
                                modelAndView.addObject("pages", pgn.getPages());
                            }

                            break;
                        default:
                            break;
                    }
                    break;
                case "biographies":
                    switch (action) {
                        case "add":
                            break;

                        case "edit":
                            Artist eArtist = artistRepository.getOne(id);
                            if (eArtist != null && eArtist.getBiography() != null){
                                modelAndView.addObject("eArtist", eArtist);
                                modelAndView.addObject("eArtistAvatar", eArtist.getImage());
                            } else {
                                isErrorFound = true;
                                modelAndView.addObject("error_code", "404");
                                modelAndView.addObject("error_context", "Oops ! Biography is not found.");
                                modelAndView.addObject("error_message", "We couldn't find biography with id: " + id);

                                modelAndView.addObject("goTo", "");

                            }

                            break;

                        case "noAction":
                            if (pageable != null){
                                Page artists = (user.isAdmin()) ? artistRepository.findAllByBiographyIsNotNull(pageable) : artistRepository.findAllByUserAndBiographyIsNotNull(user, pageable);
                                Pagination pgn = new Pagination(artists, pageable, 5);

                                modelAndView.addObject("currentPage", pgn.getPageNumber());
                                modelAndView.addObject("pageSize", pgn.getPageSize());
                                modelAndView.addObject("artists", artists.getContent());
                                modelAndView.addObject("pages", pgn.getPages());
                            }

                            break;
                        default:
                            break;
                    }
                    break;
                case "tracks":
                    switch (action) {
                        case "add":
                            break;

                        case "verify":
                            System.out.println("came there!");
                            List<Track> tracks = trackRepository.findAllByUserAndIsVerified(user, 0);
                            HashMap<Integer, String[]> trs = new HashMap<>();
                            for (Track track:tracks) {

                                Resource file = storageService.loadMusic(track.getFileName());
                                try {
                                    Mp3File mp3file = new Mp3File(file.getFile());

                                    Long length = mp3file.getLengthInSeconds();
                                    int bitrate = mp3file.getBitrate();
                                    int sampleRate = mp3file.getSampleRate();


                                    System.out.println("sample rate: " + sampleRate);
                                    track.setBitrate(bitrate);
                                    track.setDuration(length);
                                    track.setSize(file.getFile().length());
                                    track.setSampleRate(sampleRate);

                                    String trackTitle, trackName, artist, album;
                                    int year;

                                    if (mp3file.hasId3v2Tag()){

                                        ID3v2 id3v2Tag = mp3file.getId3v2Tag();

                                        trackName = id3v2Tag.getTrack();
                                        trackTitle = id3v2Tag.getTitle();
                                        artist = id3v2Tag.getArtist();
                                        album = id3v2Tag.getAlbum();
                                        if (id3v2Tag.getYear() != null )track.setYear(Integer.valueOf(id3v2Tag.getYear()));

                                        track.setName((trackTitle != null)? trackTitle : trackName);
                                        track.setUser(user);

                                        trs.put(track.getId(), new String[]{(artist != null) ? artist : "", (album != null) ? album : ""});

                                    } else if (mp3file.hasId3v1Tag()) {
                                        ID3v1 id3v1Tag = mp3file.getId3v1Tag();
                                        trackName = id3v1Tag.getTrack();
                                        trackTitle = id3v1Tag.getTitle();
                                        artist = id3v1Tag.getArtist();
                                        album = id3v1Tag.getAlbum();
                                        if (id3v1Tag.getYear() != null )track.setYear(Integer.valueOf(id3v1Tag.getYear()));

                                        track.setName((trackTitle != null)? trackTitle : trackName);
                                        track.setUser(user);

                                        trs.put(track.getId(), new String[]{(artist != null) ? artist : "", (album != null) ? album : ""});
                                    } else {
                                        trs.put(track.getId(), new String[]{"", ""});
                                    }

                                } catch (Exception e){
                                    System.out.println(e);
                                }


                            }


                            for (Track trd: tracks) {
                                System.out.println("Artist : " + trs.get(trd.getId())[0] + " | Album : " + trs.get(trd.getId())[1]);
                            }
                            modelAndView.addObject("tracks", tracks);
                            modelAndView.addObject("trs", trs);

                            break;

                        case "edit":
                            Track eTrack = trackRepository.getOne(id);
                            if (eTrack != null){
                                modelAndView.addObject("track", eTrack);
                            } else {
                                isErrorFound = true;
                                modelAndView.addObject("error_code", "404");
                                modelAndView.addObject("error_context", "Oops ! Track is not found.");
                                modelAndView.addObject("error_message", "We couldn't find track with id: " + id);

                                modelAndView.addObject("goTo", "");

                            }

                            break;

                        case "noAction":
                            if (pageable != null){
                                Page<Track> tracks_ = (user.isAdmin()) ? trackRepository.findAllByIsVerified(1, pageable) : trackRepository.findAllByUserAndIsVerified(user, 1, pageable);
                                Pagination pgn = new Pagination(tracks_, pageable, 5);

                                modelAndView.addObject("currentPage", pgn.getPageNumber());
                                modelAndView.addObject("pageSize", pgn.getPageSize());
                                modelAndView.addObject("tracks", tracks_.getContent());
                                modelAndView.addObject("pages", pgn.getPages());
                            }

                            break;
                        default:
                            break;
                    }
                    break;
                case "gallery":
                    switch (action) {
                        case "add":
                            break;

                        case "edit":

                            if (pageable != null){
                                if (id != null){
                                    Artist artist = artistRepository.getOne(id);

                                    if (artist != null){
                                        if (galleryRepository.findAllByArtist(artist).size() > 0){
                                            modelAndView.addObject("artist", artist);
                                            Page gallery = galleryRepository.findAllByArtist(artist, pageable);

                                            Pagination pgn = new Pagination(gallery, pageable, 5);

                                            modelAndView.addObject("currentPage", pgn.getPageNumber());
                                            modelAndView.addObject("pageSize", pgn.getPageSize());
                                            modelAndView.addObject("images", gallery.getContent());
                                            modelAndView.addObject("pages", pgn.getPages());
                                        } else {
                                            isErrorFound = true;
                                            modelAndView.addObject("goTo", "");
                                            modelAndView.addObject("error_code", "404");
                                            modelAndView.addObject("error_context", "Oops ! Gallery is not found.");
                                            modelAndView.addObject("error_message", "We couldn't find biography with id: " + id);

                                        }

                                    } else {
                                        isErrorFound = true;
                                        modelAndView.addObject("error_code", "404");
                                        modelAndView.addObject("error_context", "Oops ! Gallery is not found.");
                                        modelAndView.addObject("goTo", "");
                                        modelAndView.addObject("error_message", "We couldn't find biography with id: " + id);

                                    }
                                } else {
                                    isErrorFound = true;
                                    modelAndView.addObject("error_context", "Oops ! Biography is not found.");
                                    modelAndView.addObject("error_code", "404");
                                    modelAndView.addObject("error_message", "We couldn't find biography with id: " + id);

                                    modelAndView.addObject("goTo", "");
                                }
                            }

                            break;

                        case "noAction":
                            if (pageable != null){
                                Page artists = (user.isAdmin()) ? artistRepository.findAllWhereHasGallery(pageable) : artistRepository.findAllWhereHasGalleryUser(user, pageable);

                                Pagination pgn = new Pagination(artists, pageable, 5);

                                modelAndView.addObject("currentPage", pgn.getPageNumber());
                                modelAndView.addObject("pageSize", pgn.getPageSize());
                                modelAndView.addObject("artists", artists.getContent());
                                modelAndView.addObject("pages", pgn.getPages());
                            }

                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    List<Track> tracks = (user.isAdmin()) ? trackRepository.findAllByIsVerified(1) : trackRepository.findAllByUserAndIsVerified(user, 1);
                    Integer listens = 0;
                    Integer downloads = 0;
                    for (Track track:tracks) {
                        listens += track.getListens();
                        downloads += track.getDownloads();
                    }

                    if (user.isAdmin()){

                        modelAndView.addObject("allTracksListen", listens);
                        modelAndView.addObject("allTracksDownloads", downloads);
                        modelAndView.addObject("allTracks", tracks.size());

                        modelAndView.addObject("allArtists", artistRepository.findAll().size());
                        modelAndView.addObject("allAlbums", albumRepository.findAll().size());
                        modelAndView.addObject("allImages", galleryRepository.findAll().size());
                        modelAndView.addObject("allBiographies", artistRepository.findAllByBiographyIsNotNull().size());
                    } else {
                        modelAndView.addObject("allTracksListen", listens);
                        modelAndView.addObject("allTracksDownloads", downloads);
                        modelAndView.addObject("allTracks", tracks.size());

                        modelAndView.addObject("allArtists", artistRepository.findAllByUser(user).size());
                        modelAndView.addObject("allAlbums", albumRepository.findAllByUser(user).size());
                        modelAndView.addObject("allImages", galleryRepository.findAllByUser(user).size());
                        modelAndView.addObject("allBiographies", artistRepository.findAllByUserAndBiographyIsNotNull(user).size());
                    }

                    break;
            }

            modelAndView.addObject("id", id);

            CssSwitcher cssSwitcher = new CssSwitcher();
            modelAndView.addObject("cssSwitcher", cssSwitcher);

            if (!isErrorFound){

                modelAndView.addObject("error_code", "403");
                modelAndView.addObject("error_context", "Oops ! Something went wrong");
                modelAndView.addObject("error_message", "We couldn't find this page");

            }

        }
        modelAndView.addObject("avatar", user.getImage());

        return modelAndView;
    }


}