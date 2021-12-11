package com.spring.kkaemiGG.service;

import com.spring.kkaemiGG.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileService {

    private final AmazonS3Service amazonS3Service;

    public String uploadImage(MultipartFile multipartFile, User user) {
        String key = "static/images/" +
                user.getId() + "/" +
                UUID.randomUUID() + "." +
                StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());

        return amazonS3Service.putObject(key, multipartFile);
    }
}
