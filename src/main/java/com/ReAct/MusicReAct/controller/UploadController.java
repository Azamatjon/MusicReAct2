package com.ReAct.MusicReAct.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ReAct.MusicReAct.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class UploadController {

    @Autowired
    StorageService storageService;

    List<String> files = new ArrayList<String>();

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