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
import org.springframework.data.domain.PageRequest;
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



    public ModelAndView getCommonData(){
        ModelAndView modelAndView = new ModelAndView();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        Pageable topAlbums = new PageRequest(0, 4);
        Page<Album> lastAlbums = albumRepository.tops(topAlbums);
        modelAndView.addObject("lastAlbums", lastAlbums.getContent());


        Pageable top = new PageRequest(0, 6);
        Page<Artist> topArtists = artistRepository.tops(top);

        modelAndView.addObject("topArtists", topArtists);

        modelAndView.addObject("galleryRepository", galleryRepository);

        Pageable topTracks = new PageRequest(0, 50);
        modelAndView.addObject("topTracks", trackRepository.tops(topTracks));

        if (user != null){
            modelAndView.addObject("user", user);
            modelAndView.addObject("isLoggedIn", true);

            List<Track> userTracks = trackRepository.findAllByUserAndIsVerified(user, 1);

            modelAndView.addObject("userTracks", userTracks);

            modelAndView.addObject("userFavorites", user.getFavoriteTracks());

            modelAndView.addObject("userTracksNumber", userTracks.size());

        } else {
            modelAndView.addObject("userFavorites", new ArrayList<Track>());
            modelAndView.addObject("isLoggedIn", false);
        }
        return modelAndView;
    }




    @RequestMapping(value={"/"}, method = RequestMethod.GET)
    public ModelAndView indexPage(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("page", "home");

        modelAndView.addAllObjects(getCommonData().getModelMap());


        Pageable top = new PageRequest(0, 6);
        Page<Artist> topArtists = artistRepository.tops(top);
        modelAndView.addObject("lastGallery", galleryRepository.findTop9ByOrderByIdDesc(top));


        CssSwitcher cssSwitcher = new CssSwitcher();
        modelAndView.addObject("cssSwitcher", cssSwitcher);
        modelAndView.setViewName("default");
        return modelAndView;
    }


    @RequestMapping(value={"/album"}, method = RequestMethod.GET)
    public ModelAndView albumPage(@RequestParam(value = "id", required = false) Integer id){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("page", "album");
        modelAndView.addAllObjects(getCommonData().getModelMap());


        Album album = albumRepository.getOne(id);
        if (album != null){
            modelAndView.addObject("album", album);
            modelAndView.addObject("tracks", trackRepository.findAllByAlbumAndIsVerified(album, 1));

        }

        CssSwitcher cssSwitcher = new CssSwitcher();
        modelAndView.addObject("cssSwitcher", cssSwitcher);
        modelAndView.setViewName("default");
        return modelAndView;
    }

    @RequestMapping(value={"/user"}, method = RequestMethod.GET)
    public ModelAndView userPage(@RequestParam(value = "id", required = false) Integer id){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("page", "user");
        modelAndView.addAllObjects(getCommonData().getModelMap());


        User user = userRepository.getOne(id);
        if (user != null){
            modelAndView.addObject("userD", user);

            modelAndView.addObject("userAlbums", albumRepository.findAllByUser(user));

            modelAndView.addObject("userArtists", artistRepository.findAllByUser(user));

            modelAndView.addObject("userTracks", trackRepository.findAllByUserAndIsVerified(user, 1));
            modelAndView.addObject("userBiographies", artistRepository.findAllByUserAndBiographyIsNotNull(user));

            Pageable top = new PageRequest(0, 50);

            modelAndView.addObject("userLastTracks", trackRepository.findTop50ByUserAndIsVerifiedOrderByIdDesc(user, 1));
            modelAndView.addObject("userLastAlbums", albumRepository.findTop50ByUserOrderByIdDesc(user));
            modelAndView.addObject("userLastArtists", artistRepository.findTop50ByUserOrderByIdDesc(user));
            modelAndView.addObject("userLastGallery", galleryRepository.findTop50ByUserOrderByIdDesc(user));

        }

        CssSwitcher cssSwitcher = new CssSwitcher();
        modelAndView.addObject("cssSwitcher", cssSwitcher);
        modelAndView.setViewName("default");
        return modelAndView;
    }



    @RequestMapping(value={"/artist"}, method = RequestMethod.GET)
    public ModelAndView artistPage(@RequestParam(value = "id", required = false) Integer id){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("page", "artist");
        modelAndView.addAllObjects(getCommonData().getModelMap());


        Artist artist = artistRepository.getOne(id);
        if (artist != null){
            modelAndView.addObject("artist", artist);
            modelAndView.addObject("albums", albumRepository.findAllByArtist(artist));

            modelAndView.addObject("gallery", galleryRepository.findTop10ByArtistOrderByIdDesc(artist));
            modelAndView.addObject("lastTracks", trackRepository.findTop10ByArtistAndIsVerifiedOrderByIdDesc(artist, 1));

            modelAndView.addObject("numberOfTracksByAlbum", trackRepository);


            String separatedBiography = (artist.getBiography() != null) ? Jsoup.parse(artist.getBiography()).text():"";
            modelAndView.addObject("shortBiography", (separatedBiography.length() > 450) ? separatedBiography.substring(0,450) + "...":(artist.getBiography()!= null || !separatedBiography.isEmpty())?separatedBiography:null);

        }

        CssSwitcher cssSwitcher = new CssSwitcher();
        modelAndView.addObject("cssSwitcher", cssSwitcher);
        modelAndView.setViewName("default");
        return modelAndView;
    }





    @RequestMapping(value={"/search"}, method = RequestMethod.GET)
    public ModelAndView artistPage(@RequestParam(value = "query", required = false) String query){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("page", "search");
        modelAndView.addAllObjects(getCommonData().getModelMap());

        List<Album> albums = albumRepository.searchAlbums(query);
        List<Artist> artists = artistRepository.searchArtists(query);
        List<Track> tracks = trackRepository.searchTracks(query);

        modelAndView.addObject("searchAlbumResult", albums);
        modelAndView.addObject("searchArtistResult", artists);
        modelAndView.addObject("searchTrackResult", tracks);


        if (tracks.size() > 0){
            modelAndView.addObject("isTrackActive", "defaultFilter");
        } else if (artists.size() > 0) {
            modelAndView.addObject("isArtistActive", "defaultFilter");
        } else {
            modelAndView.addObject("isAlbumActive", "defaultFilter");
        }
        for (Track track: trackRepository.searchTracks(query)) {
            System.out.println("track: " + track.getArtist().getName());
        }

        CssSwitcher cssSwitcher = new CssSwitcher();
        modelAndView.addObject("cssSwitcher", cssSwitcher);
        modelAndView.setViewName("default");
        return modelAndView;
    }




    @RequestMapping(value={"/gallery"}, method = RequestMethod.GET)
    public ModelAndView galleryPage(@RequestParam(value = "id", required = false) Integer id){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("page", "gallery");
        modelAndView.addAllObjects(getCommonData().getModelMap());


        Artist artist = artistRepository.getOne(id);
        if (artist != null){
            modelAndView.addObject("artist", artist);

            modelAndView.addObject("gallery", galleryRepository.findAllByArtist(artist));
        }

        CssSwitcher cssSwitcher = new CssSwitcher();
        modelAndView.addObject("cssSwitcher", cssSwitcher);
        modelAndView.setViewName("default");
        return modelAndView;
    }

    @RequestMapping(value={"/albums"}, method = RequestMethod.GET)
    public ModelAndView albumsPage(@RequestParam(value = "artist_id", required = false) Integer id){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("page", "allAlbums");
        modelAndView.addAllObjects(getCommonData().getModelMap());

        if (id != null){
            Artist artist = artistRepository.getOne(id);

            if (artist != null){
                modelAndView.addObject("artist", artist);
                modelAndView.addObject("albums", albumRepository.findAllByArtist(artist));
                String separatedBiography = (artist.getBiography() != null) ? Jsoup.parse(artist.getBiography()).text():"";
                modelAndView.addObject("shortBiography", (separatedBiography.length() > 450) ? separatedBiography.substring(0,450) + "...":(artist.getBiography()!= null || !separatedBiography.isEmpty())?separatedBiography:null);
            }
            modelAndView.addObject("page", "artistAlbums");
        } else {
            modelAndView.addObject("albums", albumRepository.findAll());
            modelAndView.addObject("page", "allAlbums");
        }

        CssSwitcher cssSwitcher = new CssSwitcher();
        modelAndView.addObject("cssSwitcher", cssSwitcher);
        modelAndView.setViewName("default");

        return modelAndView;
    }




    @RequestMapping(value={"/topAlbums"}, method = RequestMethod.GET)
    public ModelAndView topAlbumsPage(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addAllObjects(getCommonData().getModelMap());
        modelAndView.addObject("page", "topAlbums");



        modelAndView.addObject("topAlbums", albumRepository.tops());

        CssSwitcher cssSwitcher = new CssSwitcher();
        modelAndView.addObject("cssSwitcher", cssSwitcher);
        modelAndView.setViewName("default");

        return modelAndView;
    }


    @RequestMapping(value={"/topTracks"}, method = RequestMethod.GET)
    public ModelAndView topTracksPage(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addAllObjects(getCommonData().getModelMap());
        modelAndView.addObject("page", "topTracks");



        modelAndView.addObject("topTracksD", trackRepository.tops());

        CssSwitcher cssSwitcher = new CssSwitcher();
        modelAndView.addObject("cssSwitcher", cssSwitcher);
        modelAndView.setViewName("default");

        return modelAndView;
    }

    @RequestMapping(value={"/tracks"}, method = RequestMethod.GET)
    public ModelAndView allTracksPage(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addAllObjects(getCommonData().getModelMap());
        modelAndView.addObject("page", "allTracks");



        modelAndView.addObject("allTracksD", trackRepository.findAllByIsVerified(1));

        CssSwitcher cssSwitcher = new CssSwitcher();
        modelAndView.addObject("cssSwitcher", cssSwitcher);
        modelAndView.setViewName("default");

        return modelAndView;
    }




    @RequestMapping(value={"/artists"}, method = RequestMethod.GET)
    public ModelAndView artistsPage(@RequestParam(value = "by", required = false) String by){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("page", "allArtists");
        modelAndView.addAllObjects(getCommonData().getModelMap());

        modelAndView.addObject("galleryRepository", galleryRepository);

        ArrayList<String> enLetters = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"));
        modelAndView.addObject("ENLetters", enLetters);

        if (by != null){
            modelAndView.addObject("letter", by);
            modelAndView.addObject("artists", artistRepository.findAllBy(by));
        } else {
            modelAndView.addObject("artists", artistRepository.findAll());
        }

        CssSwitcher cssSwitcher = new CssSwitcher();
        modelAndView.addObject("cssSwitcher", cssSwitcher);
        modelAndView.setViewName("default");

        return modelAndView;
    }



    @RequestMapping(value={"/topArtists"}, method = RequestMethod.GET)
    public ModelAndView topArtistsPage(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("page", "topArtists");
        modelAndView.addAllObjects(getCommonData().getModelMap());

        modelAndView.addObject("galleryRepository", galleryRepository);

        modelAndView.addObject("topArtists", artistRepository.tops());

        CssSwitcher cssSwitcher = new CssSwitcher();
        modelAndView.addObject("cssSwitcher", cssSwitcher);
        modelAndView.setViewName("default");

        return modelAndView;
    }

}