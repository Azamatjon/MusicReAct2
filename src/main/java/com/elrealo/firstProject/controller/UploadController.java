package com.elrealo.firstProject.controller;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.elrealo.firstProject.service.StorageService;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
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
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.imageio.ImageIO;


@Controller
public class UploadController {

    @Autowired
    StorageService storageService;

    List<String> files = new ArrayList<String>();

    @GetMapping("/upload")
    public String listUploadedFiles(Model model) {
        return "uploadForm";
    }

    @PostMapping("/saved")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        try {
            storageService.store(file);
            model.addAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");

            files.add(file.getOriginalFilename());
            try {
                Mp3File song = new Mp3File(StorageService.rootLocation.resolve(file.getOriginalFilename()));

                if (song.hasId3v2Tag()){
                    ID3v2 id3v2tag = song.getId3v2Tag();
                    String artist = id3v2tag.getArtist();
                    String title = id3v2tag.getTitle();

                    System.out.println("artist: " + artist + ", title: " + title);
                }
            } catch (Exception e){
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            model.addAttribute("message", "FAIL to upload " + file.getOriginalFilename() + "!");
        }
        return "uploadForm";
    }

    @GetMapping("/gellallfiles")
    public String getListFiles(Model model) {
        model.addAttribute("files",
                files.stream()
                        .map(fileName -> MvcUriComponentsBuilder
                                .fromMethodName(UploadController.class, "getFile", fileName).build().toString())
                        .collect(Collectors.toList()));
        model.addAttribute("totalFiles", "TotalFiles: " + files.size());
        return "listFiles";
    }


    @GetMapping("/music/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.loadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + file.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                .body(file);
    }
}