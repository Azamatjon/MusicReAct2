package com.ReAct.MusicReAct.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ReAct.MusicReAct.Helper.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class StorageService {

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    public static final Path rootLocationAvatars = Paths.get("uploads/avatars");
    public static final Path rootLocationArtistAvatars = Paths.get("uploads/artistAvatars");
    public static final Path rootLocationAlbumAvatars = Paths.get("uploads/albumAvatars");
    public static final Path rootLocationImages = Paths.get("uploads/images");
    public static final Path rootLocationMusics = Paths.get("uploads/musics");
    public static final Path rootLocationVideos = Paths.get("uploads/videos");
    public static final Path rootLocationGallery = Paths.get("uploads/gallery");

    RandomString randomName = new RandomString(20);

    ArrayList<String> allowedImageMimeTypes = new ArrayList<>(Arrays.asList("image/gif", "image/jpeg", "image/png", "image/jpg", "image/pjpeg"));
    ArrayList<String> allowedVideoMimeTypes = new ArrayList<>(Arrays.asList("video/x-flv", "video/mp4", "video/3gpp", "video/quicktime", "video/x-msvideo"));
    ArrayList<String> allowedMusicMimeTypes = new ArrayList<>(Arrays.asList("audio/mpeg", "audio/mp3"));

    private String getRandomizedName(MultipartFile file){
        List<String> allAllowedMimeTypes = new ArrayList<String>();
        allAllowedMimeTypes.addAll(allowedImageMimeTypes);
        allAllowedMimeTypes.addAll(allowedVideoMimeTypes);
        allAllowedMimeTypes.addAll(allowedMusicMimeTypes);

        String mimeType = file.getContentType();
        String fileName = file.getOriginalFilename();
        if (allAllowedMimeTypes.contains(mimeType)){
            return randomName.nextString() + '.' + fileName.substring(fileName.lastIndexOf(".") + 1);
        }

        return randomName.nextString() + '.' + "ndf";
    }

    public String storeAvatar(MultipartFile file){
        try {
            String randomizedFileName = getRandomizedName(file);
            Files.copy(file.getInputStream(), this.rootLocationAvatars.resolve(randomizedFileName));
            return randomizedFileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed upload avatar!"  + e.getMessage());
        }
    }


    public String storeGallery(MultipartFile file){
        try {
            String randomizedFileName = getRandomizedName(file);
            Files.copy(file.getInputStream(), this.rootLocationGallery.resolve(randomizedFileName));
            return randomizedFileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed upload gallery!"  + e.getMessage());
        }
    }

    public String storeArtistAvatar(MultipartFile file){
        try {
            String randomizedFileName = getRandomizedName(file);
            Files.copy(file.getInputStream(), this.rootLocationArtistAvatars.resolve(randomizedFileName));
            return randomizedFileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed upload avatar!"  + e.getMessage());
        }
    }

    public String storeAlbumAvatar(MultipartFile file){
        try {
            String randomizedFileName = getRandomizedName(file);
            Files.copy(file.getInputStream(), this.rootLocationAlbumAvatars.resolve(randomizedFileName));
            return randomizedFileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed upload avatar!"  + e.getMessage());
        }
    }

    public String storeImage(MultipartFile file){
        try {
            String randomizedFileName = getRandomizedName(file);
            Files.copy(file.getInputStream(), this.rootLocationImages.resolve(randomizedFileName));
            return randomizedFileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed upload image!");
        }
    }

    public String storeMusic(MultipartFile file){
        try {
            String randomizedFileName = getRandomizedName(file);
            Files.copy(file.getInputStream(), this.rootLocationMusics.resolve(randomizedFileName));
            return randomizedFileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed upload music!");
        }
    }

    public String storeVideo(MultipartFile file){
        try {
            String randomizedFileName = getRandomizedName(file);
            Files.copy(file.getInputStream(), this.rootLocationVideos.resolve(randomizedFileName));
            return randomizedFileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed upload video!");
        }
    }

    public Resource loadAvatar(String filename) {
        try {
            Path file = rootLocationAvatars.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }else{
                throw new RuntimeException("Fail to load avatar 1");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Fail to load avatar 2j");
        }
    }


    public Resource loadGallery(String filename) {
        try {
            Path file = rootLocationGallery.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }else{
                throw new RuntimeException("Fail to load gallery 1");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Fail to load gallery 2j");
        }
    }


    public Resource loadArtistAvatar(String filename) {
        try {
            Path file = rootLocationArtistAvatars.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }else{
                throw new RuntimeException("Fail to load avatar ");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Fail to load avatar 2");
        }
    }

    public Resource loadAlbumAvatar(String filename) {
        try {
            Path file = rootLocationAlbumAvatars.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }else{
                throw new RuntimeException("Fail to load avatar");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Fail to load avatar 2");
        }
    }

    public Resource loadImage(String filename) {
        try {
            Path file = rootLocationImages.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }else{
                throw new RuntimeException("Fail to load image 1");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Fail to load image 2");
        }
    }

    public Resource loadMusic(String filename) {
        try {
            Path file = rootLocationMusics.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }else{
                throw new RuntimeException("Fail to load music 1");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Fail to load music 2");
        }
    }

    public Resource loadVideo(String filename) {
        try {
            Path file = rootLocationVideos.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }else{
                throw new RuntimeException("Fail to load video 1");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Fail to load video 2");
        }
    }





    /*
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
    */
    public void init() {
        try {
            Files.createDirectory(rootLocationAvatars);
            Files.createDirectory(rootLocationImages);
            Files.createDirectory(rootLocationMusics);
            Files.createDirectory(rootLocationVideos);
            Files.createDirectory(rootLocationAlbumAvatars);
            Files.createDirectory(rootLocationVideos);
            Files.createDirectory(rootLocationGallery);

        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
        }
    }
}