package com.spring.kkaemiGG.web.controller.user;

import com.spring.kkaemiGG.service.user.UserService;
import com.spring.kkaemiGG.web.dto.user.UserSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    @PostMapping("/api/v1/user")
    public Long createUser(@RequestBody UserSaveRequestDto requestDto) {
        return userService.save(requestDto);
    }

}
