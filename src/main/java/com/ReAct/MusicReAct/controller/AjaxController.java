package com.ReAct.MusicReAct.controller;

import com.ReAct.MusicReAct.model.*;
import com.ReAct.MusicReAct.repository.*;
import com.ReAct.MusicReAct.service.MailClient;
import com.ReAct.MusicReAct.service.UserService;
import com.ReAct.MusicReAct.service.StorageService;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static org.jsoup.nodes.Document.OutputSettings.Syntax.html;

@Controller
public class AjaxController {

    @Autowired
    private UserService userService;

    @Autowired
    StorageService storageService;

    @Qualifier("artistRepository")
    @Autowired
    private ArtistRepository artistRepository;

    @Qualifier("albumRepository")
    @Autowired
    private AlbumRepository albumRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Qualifier("adminStyleRepository")
    @Autowired
    private AdminStyleRepository adminStyleRepository;

    @Qualifier("galleryRepository")
    @Autowired
    private GalleryRepository galleryRepository;

    @Qualifier("roleRepository")
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;


    @Autowired
    private MailClient mailClient;

    @Qualifier("imageRepository")
    @Autowired
    private ImageRepository imageRepository;

    @Qualifier("trackRepository")
    @Autowired
    private TrackRepository trackRepository;



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
                        User changingUser = userRepository.getById((int)userId);
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
                        User changingUser = userRepository.getById((int)userId);

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

                        if (active != null){
                            changingUser.setActive(active);
                        }

                        if (roleId != null){
                            Role role = roleRepository.findById((int)roleId);
                            if (role != null){
                                if (!role.getRole().equals(changingUser.getRoleName())){
                                    Set<Role> roles = new HashSet<>(Arrays.asList(role));
                                    changingUser.setRoles(roles);
                                }
                            } else System.out.println("Role not found id: " + roleId);
                        }

                        if (!firstName.isEmpty() && firstName != null){
                            changingUser.setName(firstName);
                        }

                        if (!lastName.isEmpty() && lastName != null){
                            changingUser.setLastName(lastName);
                        }

                        if (!newPassword.isEmpty() && newPassword != null){
                            changingUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
                        }

                        if (avatar != null){
                            System.out.println("changing avatar");
                            String generatedAvatarName = storageService.storeAvatar(avatar);
                            changingUser.setImage(generatedAvatarName);
                        }

                        userRepository.save(changingUser);

                        map.put("avatar", changingUser.getImage());
                        if (!map.containsKey("status")) map.put("status", "success");
                        map.put("redirect", "/admin/home?goTo=users");

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

                    map.put("avatar", user.getImage());
                    map.put("firstName", user.getName());
                    map.put("lastName", user.getLastName());
                    map.put("email", user.getEmail());
                }
                break;
            case "changePassword":
                if (userId != null){
                    if (user.isAdmin()){
                        User changingPasswordUser = userRepository.getById((int)userId);
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
                        User userToDelete = userRepository.getById((int)userId);
                        if (userToDelete != null){
                            userRepository.delete(userToDelete);
                            map.put("status", "success");
                            map.put("redirect", "/admin/home?goTo=users");
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











    @RequestMapping(value = "/ajax/artist", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> artistAPI(@RequestParam("action") String action,
                                         @RequestParam(value = "artistId", required = false) Integer artistId,
                                         @RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "biography", required = false) String biography,
                                         @RequestParam(value = "avatar", required = false) MultipartFile avatar,
                                         @RequestParam(value = "birthDate", required = false) String birthDate,

                                         HttpServletRequest request,
                                         HttpServletResponse response
    ) {
        HashMap<String, String> map = new HashMap<>();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        switch (action){
            case "deleteAvatar":
                if (artistId != null){
                    if (user.isAdmin() || artistRepository.getByUserAndId(user, artistId).getUser() == user){
                        Artist artist = artistRepository.getOne(artistId);
                        if (artist != null){
                            artist.setImage(null);
                            artistRepository.save(artist);

                            map.put("status", "success");
                            map.put("avatar", "/artistAvatars/default_avatar.png");
                        } else {
                            map.put("status", "error");
                            map.put("error_code", "Artist id is not found.");
                        }
                    } else {
                        map.put("status", "error");
                        map.put("error_code", "You don't have change data privilegies, to another users.");
                    }
                } else {
                    map.put("status", "error");
                    map.put("error_code", "Artist id is null");
                }
                break;
            case "edit":
                if (artistId != null){
                    if (user.isAdmin() || artistRepository.getByUserAndId(user, artistId).getUser() == user){
                        Artist artist = artistRepository.getOne(artistId);
                        if (artist != null){
                            artist.setName(name);
                            artist.setBiography(biography);
                            if (avatar != null){
                                System.out.println("changing avatar");
                                String generatedAvatarName = storageService.storeArtistAvatar(avatar);
                                artist.setImage(generatedAvatarName);
                            }

                            if (birthDate != null &&birthDate.length() > 0){
                                artist.setBirthDate(birthDate);
                            }
                            artistRepository.save(artist);
                            map.put("status", "success");
                        } else {
                            map.put("status", "error");
                            map.put("error_code", "Artist id is not found.");
                        }
                    } else {
                        map.put("status", "error");
                        map.put("error_code", "You don't have change data privilegies, to another users.");
                    }
                } else {
                    map.put("status", "error");
                    map.put("error_code", "Error!. " );
                }
                break;

            case "add":
                if (user.isAdmin() || user != null){
                    Artist artistExists = artistRepository.getByName(name);
                    if (artistExists == null){
                        Artist artist = new Artist();
                        artist.setName(name);
                        if (!biography.isEmpty()) artist.setBiography(biography);

                        if (avatar != null){
                            System.out.println("changing avatar");
                            String generatedAvatarName = storageService.storeArtistAvatar(avatar);
                            artist.setImage(generatedAvatarName);
                        }
                        artist.setUser(user);
                        if (birthDate != null &&birthDate.length() > 0){
                            artist.setBirthDate(birthDate);
                        }
                        artistRepository.save(artist);
                        map.put("status", "success");
                    } else {
                        map.put("status", "error");
                        map.put("error_code", "Artist by name (" + name + ") exists.");
                    }


                } else {
                    map.put("status", "error");
                    map.put("error_code", "You don't have enough permissions.");
                }
                break;

            case "delete":
                if (artistId != null){
                    if (user.isAdmin() || artistRepository.getByUserAndId(user, artistId).getUser() == user){
                        Artist artist = artistRepository.getOne(artistId);
                        if (artist != null){

                            List<Album> chainedAlbums = albumRepository.findAllByArtist(artist);
                            albumRepository.deleteAll(chainedAlbums);
                            artistRepository.delete(artist);


                            map.put("status", "success");
                            map.put("redirect", "/admin/home?goTo=artists");
                        } else {
                            map.put("status", "error");
                            map.put("error_code", "Artist not found.");
                        }
                    } else {
                        map.put("status", "error");
                        map.put("error_code", "You don't have enough permissions.");
                    }
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












    @RequestMapping(value = "/ajax/album", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> artistAPI(@RequestParam("action") String action,
                                         @RequestParam(value = "artistId", required = false) Integer artistId,
                                         @RequestParam(value = "albumId", required = false) Integer albumId,
                                         @RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "avatar", required = false) MultipartFile avatar,
                                         HttpServletRequest request,
                                         HttpServletResponse response
    ) {
        HashMap<String, String> map = new HashMap<>();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        switch (action){
            case "deleteAvatar":
                if (albumId != null){
                    if (user.isAdmin() || albumRepository.getByUserAndId(user, albumId).getUser() == user){
                        Album album = albumRepository.getOne(albumId);
                        if (album != null){
                            album.setImage(null);
                            albumRepository.save(album);

                            map.put("status", "success");
                            map.put("avatar", album.getImage());
                        } else {
                            map.put("status", "error");
                            map.put("error_code", "Artist id is not found.");
                        }
                    } else {
                        map.put("status", "error");
                        map.put("error_code", "You don't have enough privilegies.");
                    }
                } else {
                    map.put("status", "error");
                    map.put("error_code", "Album id is null");
                }
                break;
            case "edit":
                if (albumId != null){
                    if (user.isAdmin() || albumRepository.getByUserAndId(user, albumId).getUser() == user){
                        Album album = albumRepository.getOne(albumId);
                        if (album != null){
                            album.setName(name);

                            Artist artist = artistRepository.getOne(artistId);

                            if (artist != null){
                                album.setArtist(artist);
                                System.out.println("artist was set successfuly");
                            }

                            if (avatar != null){
                                System.out.println("changing avatar");
                                String generatedAvatarName = storageService.storeAlbumAvatar(avatar);
                                album.setImage(generatedAvatarName);
                            }
                            albumRepository.save(album);
                            map.put("status", "success");
                        } else {
                            map.put("status", "error");
                            map.put("error_code", "Album id is not found.");
                        }
                    } else {
                        map.put("status", "error");
                        map.put("error_code", "You don't have change data privilegies.");
                    }
                } else {
                    map.put("status", "error");
                    map.put("error_code", "Error!. " );
                }
                break;

            case "add":
                if (user.isAdmin() || user != null){

                    Artist artist = artistRepository.getOne(artistId);
                    if (artist != null){
                        Album albumExists = albumRepository.getByNameAndArtist(name, artist);


                        //System.out.println("album exists: " + albumExists.getName());
                        if (albumExists == null){
                            Album album = new Album();
                            album.setName(name);
                            album.setArtist(artist);
                            album.setUser(user);
                            if (avatar != null){
                                System.out.println("changing avatar");
                                String generatedAvatarName = storageService.storeAlbumAvatar(avatar);
                                album.setImage(generatedAvatarName);
                            }
                            albumRepository.save(album);
                            map.put("status", "success");
                        } else {
                            map.put("status", "error");
                            map.put("error_code", "Album by name (" + name + ") of " + artist.getName() + " exists.");
                        }
                    } else {
                        map.put("status", "error");
                        map.put("error_code", "Album id not found exists.");
                    }



                } else {
                    map.put("status", "error");
                    map.put("error_code", "You don't have enough permissions.");
                }
                break;

            case "delete":
                if (albumId != null){
                    if (user.isAdmin() || albumRepository.getByUserAndId(user, albumId).getUser() == user){
                        Album album = albumRepository.getOne(albumId);
                        if (album != null){
                            albumRepository.delete(album);
                            map.put("status", "success");
                            map.put("redirect", "/admin/home?goTo=albums");
                        } else {
                            map.put("status", "error");
                            map.put("error_code", "Album not found.");
                        }
                    } else {
                        map.put("status", "error");
                        map.put("error_code", "You don't have enough permissions.");
                    }
                } else {
                    map.put("status", "error");
                    map.put("error_code", "Album id is not found.");
                }

                break;

            default:
                map.put("status", "error");
                map.put("error_code", "No such action defined.");

                break;
        }

        return map;
    }











    @RequestMapping(value = "/ajax/biography", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> artistAPI(@RequestParam("action") String action,
                                         @RequestParam(value = "artistId", required = false) Integer artistId,
                                         @RequestParam(value = "biography", required = false) String biography,
                                         HttpServletRequest request,
                                         HttpServletResponse response
    ) {
        HashMap<String, String> map = new HashMap<>();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        switch (action){
            case "edit":
                if (artistId != null){
                    if (user.isAdmin() || artistRepository.getByUserAndId(user, artistId).getUser() == user){
                        Artist artist = artistRepository.getOne(artistId);
                        if (artist != null){
                            if (artist.getBiography() == null){
                                map.put("status", "error");
                                map.put("error_code", "Artist doesn't have biography.");
                            } else {
                                artist.setBiography(biography);
                                artistRepository.save(artist);
                                map.put("status", "success");
                            }


                        } else {
                            map.put("status", "error");
                            map.put("error_code", "Artist id is not found.");
                        }
                    } else {
                        map.put("status", "error");
                        map.put("error_code", "You don't have change data privilegies.");
                    }
                } else {
                    map.put("status", "error");
                    map.put("error_code", "Error!. " );
                }
                break;

            case "add":
                if (user.isAdmin()){

                    Artist artist = artistRepository.getOne(artistId);
                    if (artist != null){
                        if (artist.getBiography() != null){
                            map.put("status", "error");
                            map.put("error_code", "Artist already has biography.");
                        } else {
                            artist.setBiography(biography);
                            artistRepository.save(artist);
                            map.put("status", "success");
                        }
                    } else {
                        map.put("status", "error");
                        map.put("error_code", "Album id not found.");
                    }



                } else {
                    map.put("status", "error");
                    map.put("error_code", "You don't have enough permissions.");
                }
                break;

            case "delete":
                if (artistId != null){
                    if (user.isAdmin() || artistRepository.getByUserAndId(user, artistId).getUser() == user){
                        Artist artist = artistRepository.getOne(artistId);
                        if (artist != null){
                            artist.setBiography(null);
                            artistRepository.save(artist);
                            map.put("status", "success");
                            map.put("redirect", "/admin/home?goTo=biographies");

                        } else {
                            map.put("status", "error");
                            map.put("error_code", "Artist not found.");
                        }
                    } else {
                        map.put("status", "error");
                        map.put("error_code", "You don't have enough permissions.");
                    }
                } else {
                    map.put("status", "error");
                    map.put("error_code", "Album id is not found.");
                }

                break;

            default:
                map.put("status", "error");
                map.put("error_code", "No such action defined.");

                break;
        }

        return map;
    }





    @RequestMapping(value = "/ajax/gallery", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> galleryAPI(@RequestParam("action") String action,
                                         @RequestParam(value = "artistId", required = false) Integer artistId,
                                          @RequestParam(value = "imageId", required = false) Integer imageId,
                                         HttpServletRequest request,
                                         HttpServletResponse response
    ) {
        HashMap<String, String> map = new HashMap<>();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        switch (action){

            case "deleteAll":
                if (artistId != null){
                    if (user.isAdmin() || user != null){
                        Artist artist = artistRepository.getOne(artistId);
                        if (artist != null){
                            List<Gallery> images = galleryRepository.findAllByArtist(artist);
                            galleryRepository.deleteAll(images);

                            map.put("status", "success");
                            map.put("redirect", "/admin/home?goTo=gallery");

                        } else {
                            map.put("status", "error");
                            map.put("error_code", "Artist not found.");
                        }
                    } else if (user != null){
                        Artist artist = artistRepository.getOne(artistId);
                        if (artist != null){
                            List<Gallery> images = galleryRepository.findAllByArtistAndUser(artist, user);
                            galleryRepository.deleteAll(images);

                            map.put("status", "success");
                            map.put("redirect", "/admin/home?goTo=gallery");

                        } else {
                            map.put("status", "error");
                            map.put("error_code", "Artist not found.");
                        }
                    } else {
                        map.put("status", "error");
                        map.put("error_code", "You don't have enough permissions.");
                    }
                } else {
                    map.put("status", "error");
                    map.put("error_code", "Album id is not found.");
                }

                break;

            case "delete":
                if (imageId != null){
                    if (user.isAdmin() || galleryRepository.getByUserAndId(user, imageId).getUser() == user){
                        Gallery image = galleryRepository.getOne(imageId);
                        if (image != null){
                            galleryRepository.delete(image);
                            map.put("status", "success");
                            map.put("redirect", "/admin/home?goTo=biographies");

                        } else {
                            map.put("status", "error");
                            map.put("error_code", "Image not found.");
                        }
                    } else if (user != null){

                        Gallery image = galleryRepository.getByIdAndUser(imageId, user);
                        if (image != null){
                            galleryRepository.delete(image);
                            map.put("status", "success");

                        } else {
                            map.put("status", "error");
                            map.put("error_code", "Image not found.");
                        }
                    } else {
                        map.put("status", "error");
                        map.put("error_code", "You don't have enough permissions.");
                    }
                } else {
                    map.put("status", "error");
                    map.put("error_code", "Image id is not found.");
                }

                break;


            default:
                map.put("status", "error");
                map.put("error_code", "No such action defined.");

                break;
        }

        return map;
    }




    @RequestMapping(value = "/ajax/favorite", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> galleryAPI(@RequestParam("action") String action,
                                          @RequestParam(value = "trackId", required = false) Integer trackId,
                                          HttpServletRequest request,
                                          HttpServletResponse response
    ) {
        HashMap<String, String> map = new HashMap<>();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        switch (action){
            case "common":
                if (trackId != null){
                    if (user != null){
                        Track track = trackRepository.getOne(trackId);
                        if (track != null){
                            Set<Track> tracks = user.getFavoriteTracks();
                            if (tracks.contains(track)){
                                tracks.remove(track);
                                map.put("message", "removed");
                            } else {
                                tracks.add(track);
                                map.put("message", "added");
                            }
                            userRepository.save(user);

                            map.put("status", "success");

                        } else {
                            map.put("status", "error");
                            map.put("error_code", "Track is not found.");
                        }
                    } else {
                        map.put("status", "error");
                        map.put("error_code", "You don't have enough permissions.");
                    }
                } else {
                    map.put("status", "error");
                    map.put("error_code", "Track id is not found.");
                }

                break;

            default:
                map.put("status", "error");
                map.put("error_code", "No such action defined.");

                break;
        }

        return map;
    }


    @RequestMapping(value = "/ajax/track", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> trackAPI(@RequestParam("action") String action,
                                         @RequestParam(value = "trackId", required = false) Integer trackId,
                                         @RequestParam(value = "trackName", required = false) String trackName,
                                         @RequestParam(value = "artistName", required = false) String artistName,
                                         @RequestParam(value = "albumName", required = false) String albumName,
                                         @RequestParam(value = "year", required = false) Integer year,
                                         @RequestParam(value = "duration", required = false) Long duration,
                                         @RequestParam(value = "size", required = false) Long size,
                                         @RequestParam(value = "bitrate", required = false) Integer bitrate,
                                         @RequestParam(value = "sampleRate", required = false) Integer sampleRate,
                                         HttpServletRequest request,
                                         HttpServletResponse response
    ) {
        HashMap<String, String> map = new HashMap<>();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        switch (action){
            case "edit":
                if (trackId != null){
                    if (user.isAdmin() || trackRepository.getByUserAndId(user, trackId).getUser() == user){
                        Track track = trackRepository.getOne(trackId);
                        if (track != null){
                            track.setName(trackName);
                            track.setYear(year);

                            Artist artist = artistRepository.getByName(artistName);
                            if (artist != null){
                                track.setArtist(artist);
                            } else {
                                Artist artist1 = new Artist();
                                artist1.setName(artistName);

                                artistRepository.save(artist1);

                                Artist artist2 = artistRepository.getByName(artistName);
                                track.setArtist(artist2);
                            }



                            Album album = albumRepository.getByNameAndArtist(albumName, track.getArtist());
                            if (album != null){
                                track.setAlbum(album);
                            } else {
                                Album album1 = new Album();
                                album1.setName(albumName);

                                albumRepository.save(album1);

                                Album album2 = albumRepository.getByNameAndArtist(albumName, track.getArtist());
                                track.setAlbum(album2);
                            }

                            trackRepository.save(track);
                            map.put("status", "success");
                        } else {
                            map.put("status", "error");
                            map.put("error_code", "Artist id is not found.");
                        }
                    } else {
                        map.put("status", "error");
                        map.put("error_code", "You don't have change data privilegies.");
                    }
                } else {
                    map.put("status", "error");
                    map.put("error_code", "Error!. " );
                }
                break;

            case "verify":
                if (user.isAdmin() || trackRepository.getByUserAndId(user, trackId).getUser() == user){

                    Track track = trackRepository.getOne(trackId);

                    if (track != null){

                        track.setName(trackName);


                        Artist artist = artistRepository.getByName(artistName);
                        if (artist == null){
                            Artist artist1 = new Artist();
                            artist1.setName(artistName);
                            artist1.setUser(user);
                            artistRepository.save(artist1);

                            Artist artist2 = artistRepository.getByName(artistName);
                            track.setArtist(artist2);

                        } else {
                            track.setArtist(artist);
                        }



                        Album album = albumRepository.getByNameAndArtist(albumName, track.getArtist());
                        if (album == null){
                            Album album1 = new Album();
                            album1.setArtist(track.getArtist());
                            album1.setName(albumName);
                            album1.setUser(user);

                            albumRepository.save(album1);

                            Album album2 = albumRepository.getByNameAndArtist(albumName, track.getArtist());
                            track.setAlbum(album2);

                        } else {
                            track.setAlbum(album);
                        }

                        if (year > 0) track.setYear(year);

                        track.setDuration(duration);
                        track.setSize(size);
                        track.setBitrate(bitrate);
                        track.setSampleRate(sampleRate);

                        track.setIsVerified(true);

                        trackRepository.save(track);

                        map.put("status", "success");
                    } else {
                        map.put("status", "error");
                        map.put("error_code", "Track id not found.");
                    }

                } else {
                    map.put("status", "error");
                    map.put("error_code", "You don't have enough permissions.");
                }
                break;

            case "delete":
                if (trackId != null){
                    if (user.isAdmin() || trackRepository.getByUserAndId(user, trackId).getUser() == user){
                        Track track = trackRepository.getOne(trackId);
                        if (track != null){
                            trackRepository.delete(track);
                            map.put("status", "success");
                            map.put("redirect", "/admin/home?goTo=tracks");

                        } else {
                            map.put("status", "error");
                            map.put("error_code", "Track not found.");
                        }
                    } else {
                        map.put("status", "error");
                        map.put("error_code", "You don't have enough permissions.");
                    }
                } else {
                    map.put("status", "error");
                    map.put("error_code", "Track id is not found.");
                }

                break;

            default:
                map.put("status", "error");
                map.put("error_code", "No such action defined.");

                break;
        }

        return map;
    }










    @RequestMapping(value = "/ajax/liteSearch", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> liteSearchAPI(@RequestParam("option") String option,
                                             @RequestParam(value = "action", required = false) String action,
                                             @RequestParam(value = "query", required = false) String query,
                                             @PageableDefault(sort = {"name"}, direction = Sort.Direction.DESC, page = 1, size = 10,  value = 10) Pageable pageable
    ) {
        HashMap<String, Object> map = new HashMap<>();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        switch (option){
            case "user":
                if (user != null){
                   Page<User> users = userRepository.liteSearch(query, pageable);

                    List<HashMap<String, Object>> results = new ArrayList<>();


                    //System.out.println(users.getTotalElements());
                    for (User us:users.getContent()) {
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("id", us.getId());
                        result.put("email", us.getEmail());
                        result.put("firstName", us.getName());
                        result.put("lastName", us.getLastName());
                        result.put("role", us.getRoleName());
                        result.put("imageURL", us.getImage());
                        results.add(result);
                    }

                    map.put("results", results);

                    map.put("incomplete_results", users.hasNext());
                    map.put("total_count", users.getTotalElements());

                    map.put("status", "success");
                } else {
                    map.put("status", "error");
                    map.put("error_code", "You don't have privilegies.");
                }
                break;
            case "artist":
                if (user != null){

                    System.out.println(action);
                    Page<Artist> artists;
                    if (action != null && action.equals("biography")){
                        artists = artistRepository.liteSearchBiography(query, pageable);
                    } else {
                        artists = artistRepository.liteSearch(query, pageable);
                    }

                    List<HashMap<String, Object>> results = new ArrayList<>();

                    //System.out.println(users.getTotalElements());
                    for (Artist artist:artists.getContent()) {
                        System.out.println("worked");
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("id", artist.getId());
                        result.put("name", artist.getName());
                        result.put("numberOfAlbums", artist.getNumberOfAlbums());
                        result.put("numberOfTracks", artist.getNumberOfTracks());
                        result.put("imageURL", artist.getImage());

                        result.put("age", (artist.getBirthDate() != null)? ((Calendar.getInstance().getTime().getYear()) -  artist.getBirthDate().getYear()):null);

                        String separatedBiography = (artist.getBiography() != null) ? Jsoup.parse(artist.getBiography()).text():"";
                        result.put("biography", (separatedBiography.length() > 50) ? separatedBiography.substring(0,50) + "...":(artist.getBiography()!= null || !separatedBiography.isEmpty())?separatedBiography:null);
                        results.add(result);
                    }

                    map.put("results", results);

                    map.put("incomplete_results", artists.hasNext());
                    map.put("total_count", artists.getTotalElements());

                    map.put("status", "success");
                } else {
                    map.put("status", "error");
                    map.put("error_code", "You don't have privilegies.");
                }
                break;

            default:
                map.put("status", "error");
                map.put("error_code", "No such action defined.");

                break;
        }

        return map;
    }














    @RequestMapping(value = "/ajax/image", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> artistAPI(@RequestParam("action") String action,
                                         @RequestParam(value = "image", required = false) MultipartFile image,
                                         @RequestParam(value = "src", required = false) String src,
                                         HttpServletRequest request,
                                         HttpServletResponse response
    ) {
        HashMap<String, String> map = new HashMap<>();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        switch (action){
            case "delete":
                if (user != null){
                    String[] temp = src.split("/");
                    String fileName = temp[temp.length - 1];
                    Image image_ = imageRepository.getByFileName(fileName);
                    if (image != null){
                        imageRepository.delete(image_);
                        map.put("status", "ok");
                    } else {
                        map.put("status", "error");
                        map.put("error_code", "Not found.");
                    }
                } else {
                    map.put("status", "error");
                    map.put("error_code", "You don't have upload image privilegies.");
                }
                break;
            case "add":
                if (user != null){
                    if (image != null){
                        Image image_ = new Image();

                        System.out.println("adding image");
                        String generatedImageName = storageService.storeImage(image);
                        image_.setFileName(generatedImageName);
                        imageRepository.save(image_);

                        map.put("status", "success");
                        map.put("filename", image_.getFileName());

                    } else {
                        map.put("status", "error");
                        map.put("error_code", "Image is null.");
                    }
                } else {
                    map.put("status", "error");
                    map.put("error_code", "You don't have upload image privilegies.");
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