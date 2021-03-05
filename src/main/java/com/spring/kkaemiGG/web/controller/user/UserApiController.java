package com.spring.kkaemiGG.web.controller.user;

import com.spring.kkaemiGG.service.user.UserService;
import com.spring.kkaemiGG.web.dto.user.UserSaveRequestDto;
import com.spring.kkaemiGG.web.dto.user.UsersDuplicateCheckResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    @GetMapping("/api/v1/users/{type}/{value}")
    public UsersDuplicateCheckResponseDto duplicateCheck(
            @PathVariable String type,
            @PathVariable String value) {
        return userService.findByType(type, value);
    }

    @PostMapping("/api/v1/users")
    public Long createUser(@RequestBody UserSaveRequestDto requestDto) {
        return userService.save(requestDto);
    }


}
