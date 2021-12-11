package com.spring.kkaemiGG.web.controller;

import com.spring.kkaemiGG.annotation.CustomRestController;
import com.spring.kkaemiGG.domain.user.User;
import com.spring.kkaemiGG.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@CustomRestController
public class FileController {

    private final FileService fileService;

    @PostMapping("/v1/images")
    public String uploadImage(
            @RequestParam("image") MultipartFile multipartFile,
            @AuthenticationPrincipal User user
    ) {
        return fileService.uploadImage(multipartFile, user);
    }

}
