package com.elrealo.firstProject.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;


@Service
public class StorageService {

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    public static final Path rootLocationAvatars = Paths.get("uploads/avatars");
    public static final Path rootLocationImages = Paths.get("uploads/images");
    public static final Path rootLocationMusics = Paths.get("uploads/musics");
    public static final Path rootLocationVideos = Paths.get("uploads/videos");

    public void storeAvatar(MultipartFile file){
        try {
            Files.copy(file.getInputStream(), this.rootLocationAvatars.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Failed upload avatar!");
        }
    }

    public void storeImage(MultipartFile file){
        try {
            Files.copy(file.getInputStream(), this.rootLocationImages.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Failed upload image!");
        }
    }

    public void storeMusic(MultipartFile file){
        try {
            Files.copy(file.getInputStream(), this.rootLocationMusics.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Failed upload music!");
        }
    }

    public void storeVideo(MultipartFile file){
        try {
            Files.copy(file.getInputStream(), this.rootLocationVideos.resolve(file.getOriginalFilename()));
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

        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
        }
    }
}