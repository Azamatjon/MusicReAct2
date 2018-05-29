package com.ReAct.MusicReAct.controller;

import com.ReAct.MusicReAct.Helper.CssSwitcher;
import com.ReAct.MusicReAct.Helper.Pagination;
import com.ReAct.MusicReAct.model.*;
import com.ReAct.MusicReAct.repository.*;
import com.ReAct.MusicReAct.service.MailClient;
import com.ReAct.MusicReAct.service.StorageService;
import com.ReAct.MusicReAct.service.UserService;
import com.ReAct.MusicReAct.service.UserServiceImpl;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.naming.AuthenticationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Controller
public class HomeController {

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

    @Qualifier("galleryRepository")
    @Autowired
    private GalleryRepository galleryRepository;

    @Qualifier("trackRepository")
    @Autowired
    private TrackRepository trackRepository;


    @Qualifier("artistRepository")
    @Autowired
    private ArtistRepository artistRepository;

    private RequestCache requestCache = new HttpSessionRequestCache();

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private StorageService storageService;

    @RequestMapping(value={"/"}, method = RequestMethod.GET)
    public ModelAndView indexPage(){
        ModelAndView modelAndView = new ModelAndView();


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        if (user != null){
            modelAndView.addObject("user", user);

            modelAndView.addObject("isLoggedIn", true);

            List<Track> userTracks = trackRepository.findAllByUserAndIsVerified(user, 1);
            List<Album> lastAlbums = albumRepository.findTop4ByOrderByIdDesc();

            modelAndView.addObject("favoriteTracks", user.getFavoriteTracks());
            modelAndView.addObject("albums", lastAlbums);




            modelAndView.addObject("userTracksNumber", userTracks.size());



        } else {
            modelAndView.addObject("isLoggedIn", false);
        }

        modelAndView.setViewName("index");
        return modelAndView;
    }


    @RequestMapping(value={"/album"}, method = RequestMethod.GET)
    public ModelAndView albumPage(@RequestParam(value = "id", required = false) Integer id){
        ModelAndView modelAndView = new ModelAndView();


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        if (user != null){
            modelAndView.addObject("user", user);

            modelAndView.addObject("isLoggedIn", true);

            List<Track> userTracks = trackRepository.findAllByUserAndIsVerified(user, 1);
            List<Album> lastAlbums = albumRepository.findTop4ByOrderByIdDesc();

            System.out.println("size: " + lastAlbums.size());





            modelAndView.addObject("userTracksNumber", userTracks.size());



        } else {
            modelAndView.addObject("isLoggedIn", false);
        }


        Album album = albumRepository.getOne(id);
        if (album != null){
            modelAndView.addObject("album", album);
            modelAndView.addObject("tracks", trackRepository.findAllByAlbumAndIsVerified(album, 1));

        }

        modelAndView.setViewName("album");
        return modelAndView;
    }



    @RequestMapping(value={"/artist"}, method = RequestMethod.GET)
    public ModelAndView artistPage(@RequestParam(value = "id", required = false) Integer id){
        ModelAndView modelAndView = new ModelAndView();


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        if (user != null){
            modelAndView.addObject("user", user);

            modelAndView.addObject("isLoggedIn", true);

            List<Track> userTracks = trackRepository.findAllByUserAndIsVerified(user, 1);
            List<Album> lastAlbums = albumRepository.findTop4ByOrderByIdDesc();

            System.out.println("size: " + lastAlbums.size());





            modelAndView.addObject("userTracksNumber", userTracks.size());



        } else {
            modelAndView.addObject("isLoggedIn", false);
        }


        Artist artist = artistRepository.getOne(id);
        if (artist != null){
            modelAndView.addObject("artist", artist);
            modelAndView.addObject("albums", albumRepository.findAllByArtist(artist));

            modelAndView.addObject("gallery", galleryRepository.findAllByArtist(artist));
            modelAndView.addObject("lastTracks", trackRepository.findTop10ByArtistAndIsVerifiedOrderByIdDesc(artist, 1));

            modelAndView.addObject("numberOfTracksByAlbum", trackRepository);


            String separatedBiography = (artist.getBiography() != null) ? Jsoup.parse(artist.getBiography()).text():"";
            modelAndView.addObject("shortBiography", (separatedBiography.length() > 450) ? separatedBiography.substring(0,450) + "...":(artist.getBiography()!= null || !separatedBiography.isEmpty())?separatedBiography:null);

        }

        modelAndView.setViewName("artist");
        return modelAndView;
    }



    @RequestMapping(value={"/albums"}, method = RequestMethod.GET)
    public ModelAndView albumsPage(@RequestParam(value = "artist_id", required = false) Integer id){
        ModelAndView modelAndView = new ModelAndView();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        if (user != null){
            modelAndView.addObject("user", user);

            modelAndView.addObject("isLoggedIn", true);

            List<Track> userTracks = trackRepository.findAllByUserAndIsVerified(user, 1);
            List<Album> lastAlbums = albumRepository.findTop4ByOrderByIdDesc();

            System.out.println("size: " + lastAlbums.size());

            modelAndView.addObject("userTracksNumber", userTracks.size());



        } else {
            modelAndView.addObject("isLoggedIn", false);
        }

        if (id != null){
            Artist artist = artistRepository.getOne(id);

            if (artist != null){
                modelAndView.addObject("artist", artist);
                modelAndView.addObject("albums", albumRepository.findAllByArtist(artist));
                String separatedBiography = (artist.getBiography() != null) ? Jsoup.parse(artist.getBiography()).text():"";
                modelAndView.addObject("shortBiography", (separatedBiography.length() > 450) ? separatedBiography.substring(0,450) + "...":(artist.getBiography()!= null || !separatedBiography.isEmpty())?separatedBiography:null);
            }
            modelAndView.setViewName("artistAlbums");
        } else {
            modelAndView.addObject("albums", albumRepository.findAll());
            modelAndView.setViewName("allAlbums");
        }



        return modelAndView;
    }





    @RequestMapping(value={"/artists"}, method = RequestMethod.GET)
    public ModelAndView artistsPage(@RequestParam(value = "by", required = false) String by){
        ModelAndView modelAndView = new ModelAndView();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        if (user != null){
            modelAndView.addObject("user", user);

            modelAndView.addObject("isLoggedIn", true);

            List<Track> userTracks = trackRepository.findAllByUserAndIsVerified(user, 1);
            List<Album> lastAlbums = albumRepository.findTop4ByOrderByIdDesc();

            System.out.println("size: " + lastAlbums.size());





            modelAndView.addObject("userTracksNumber", userTracks.size());



        } else {
            modelAndView.addObject("isLoggedIn", false);
        }
        modelAndView.addObject("galleryRepository", galleryRepository);

        ArrayList<String> enLetters = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"));

        modelAndView.addObject("ENLetters", enLetters);

        if (by != null){
            modelAndView.addObject("letter", by);
            modelAndView.addObject("artists", artistRepository.findAllBy(by));
        } else {
            modelAndView.addObject("artists", artistRepository.findAll());
        }

        modelAndView.setViewName("allArtists");



        return modelAndView;
    }

}