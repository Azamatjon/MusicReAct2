package com.ReAct.MusicReAct.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;

import com.ReAct.MusicReAct.model.Track;
import com.ReAct.MusicReAct.model.User;
import com.ReAct.MusicReAct.repository.TrackRepository;
import com.ReAct.MusicReAct.service.StorageService;
import com.ReAct.MusicReAct.service.UserService;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


@Controller
public class UploadController {

    @Autowired
    StorageService storageService;

    List<String> files = new ArrayList<String>();


    @Autowired
    UserService userService;

    @Qualifier("trackRepository")
    @Autowired
    TrackRepository trackRepository;

    @GetMapping("/upload")
    public String listUploadedFiles(Model model) {
        return "uploadForm";
    }
/*
    @PostMapping("/saved")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        try {
            storageService.store(file);
            model.addAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");

            files.add(file.getOriginalFilename());


        } catch (Exception e) {
            System.out.println(e.getMessage());
            model.addAttribute("message", "FAIL to upload " + file.getOriginalFilename() + "!");
        }
        return "uploadForm";
    }
*/
    @PostMapping("/upload")
    @ResponseBody
    public Map<String, String> handleFileUpload(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam(value = "type", required = false) String type) {
        boolean isUploaded = true;
        try {
            switch (type){
                case "avatar":
                    storageService.storeAvatar(file);
                    break;
                case "music":
                    storageService.storeMusic(file);
                    break;
                case "image":
                    storageService.storeImage(file);
                    break;
                case "video":
                    storageService.storeVideo(file);
                    break;
                default:
                    isUploaded = false;
                    break;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            isUploaded = false;
        }

        HashMap<String, String> map = new HashMap<>();

        if (isUploaded){
            map.put("status", "ok");
        } else {
            map.put("status", "error");
        }
        return map;
    }








    @PostMapping("/mp3upload")
    @ResponseBody
    public Map<String, String>  mp3Upload(HttpServletResponse response,
                                         @RequestParam(value = "file", required = false) MultipartFile file, @RequestParam(value = "type", required = false) String type) {
        HashMap<String, String> map = new HashMap<>();

        ArrayList<String> allowedMusicMimeTypes = new ArrayList<>(Arrays.asList("audio/mpeg", "audio/mp3"));

        System.out.println("size: " + file.getSize());
        response.setStatus(403);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        if (user != null) {
            if (file.getSize() > 20240000){
                map.put("error", "File size is too big");
            } else if (!allowedMusicMimeTypes.contains(file.getContentType())) {
                map.put("error", "File must be in mp3 format.");
            } else {
                response.setStatus(200);

                Track track = new Track();

                String generatedTrackName = storageService.storeMusic(file);
                track.setFileName(generatedTrackName);

                track.setOldFileName(file.getOriginalFilename());

                track.setUser(user);

                trackRepository.save(track);

/*
            try {
                File convFile = new File( file.getOriginalFilename());
                file.transferTo(convFile);
                Mp3File mp3file = new Mp3File(convFile);

                System.out.println("Length of this mp3 is: " + mp3file.getLengthInSeconds() + " seconds");
                System.out.println("Bitrate: " + mp3file.getBitrate() + " kbps " + (mp3file.isVbr() ? "(VBR)" : "(CBR)"));
                System.out.println("Sample rate: " + mp3file.getSampleRate() + " Hz");
                System.out.println("Has ID3v1 tag?: " + (mp3file.hasId3v1Tag() ? "YES" : "NO"));
                System.out.println("Has ID3v2 tag?: " + (mp3file.hasId3v2Tag() ? "YES" : "NO"));
                System.out.println("Has custom tag?: " + (mp3file.hasCustomTag() ? "YES" : "NO"));

                if (mp3file.hasId3v1Tag()) {
                    ID3v1 id3v1Tag = mp3file.getId3v1Tag();
                    System.out.println("Track: " + id3v1Tag.getTrack());
                    System.out.println("Artist: " + id3v1Tag.getArtist());
                    System.out.println("Title: " + id3v1Tag.getTitle());
                    System.out.println("Album: " + id3v1Tag.getAlbum());
                    System.out.println("Year: " + id3v1Tag.getYear());
                    System.out.println("Genre: " + id3v1Tag.getGenre() + " (" + id3v1Tag.getGenreDescription() + ")");
                    System.out.println("Comment: " + id3v1Tag.getComment());
                }

                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

            } catch (IOException e){
                System.out.println(e);
            } catch (UnsupportedTagException e){
                System.out.println(e);
            } catch (InvalidDataException e){
                System.out.println(e);
            }
*/

                map.put("error", "success");
            }
        } else {
            map.put("error", "You haven't authorized yet.");
        }


        return map;
    }














    @GetMapping("/musics/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getMusic(@PathVariable String filename) {
        Resource file = storageService.loadMusic(filename);
        String mimeType = "";
        try {
            mimeType = Files.probeContentType(file.getFile().toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + file.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, mimeType)
                .body(file);
    }

    @GetMapping("/artistImages/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getArtistImage(@PathVariable String filename) {
        Resource file = storageService.loadArtistAvatar(filename);
        String mimeType = "";
        try {
            mimeType = Files.probeContentType(file.getFile().toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + file.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, mimeType)
                .body(file);
    }

    @GetMapping("/albumImages/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getAlbumImage(@PathVariable String filename) {
        Resource file = storageService.loadAlbumAvatar(filename);
        String mimeType = "";
        try {
            mimeType = Files.probeContentType(file.getFile().toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + file.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, mimeType)
                .body(file);
    }



    @GetMapping("/avatars/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getAvatar(@PathVariable String filename) {
        Resource file = storageService.loadAvatar(filename);
        String mimeType = "";
        try {
            mimeType = Files.probeContentType(file.getFile().toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + file.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, mimeType)
                .body(file);
    }

    @GetMapping("/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        Resource file = storageService.loadImage(filename);
        String mimeType = "";
        try {
            mimeType = Files.probeContentType(file.getFile().toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + file.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, mimeType)
                .body(file);
    }

    @GetMapping("/videos/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getVideo(@PathVariable String filename) {
        Resource file = storageService.loadVideo(filename);
        String mimeType = "";
        try {
            mimeType = Files.probeContentType(file.getFile().toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + file.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, mimeType)
                .body(file);
    }


}