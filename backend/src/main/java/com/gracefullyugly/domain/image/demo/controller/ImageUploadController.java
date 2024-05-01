package com.gracefullyugly.domain.image.demo.controller;

import com.gracefullyugly.domain.image.demo.service.ImageUploadService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ImageUploadController {

    private final ImageUploadService imageUploadService;

    @Autowired
    public ImageUploadController(ImageUploadService imageUploadService) {
        this.imageUploadService = imageUploadService;
    }

    @PostMapping("/upload") //multipartFile null check 요망
    public String upload(MultipartFile image, Model model) throws IOException {
        String imageUrl = imageUploadService.saveFile(image);

        model.addAttribute("imageUrl", imageUrl);

        return "image";
    }
}
