package com.spring.kkaemiGG.web.controller.user;

import com.spring.kkaemiGG.service.user.UserService;
import com.spring.kkaemiGG.web.dto.user.UserSaveRequestDto;
import com.spring.kkaemiGG.web.dto.user.UsersDuplicateCheckResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    @GetMapping("/api/v1/users/email/{email}")
    public ResponseEntity<UsersDuplicateCheckResponseDto> checkEmail(@PathVariable String email) {

        UsersDuplicateCheckResponseDto dto = userService.findByEmail(email);

        if (dto.getSuccess()) {
            return ResponseEntity.ok(dto);
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(dto);
    }

    @GetMapping("/api/v1/users/nickname/{nickname}")
    public ResponseEntity<UsersDuplicateCheckResponseDto> checkNickname(@PathVariable String nickname) {

        UsersDuplicateCheckResponseDto dto = userService.findByNickname(nickname);

        if (dto.getSuccess()) {
            return ResponseEntity.ok(dto);
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(dto);
    }

    @PostMapping("/api/v1/user")
    public Long createUser(@RequestBody UserSaveRequestDto requestDto) {
        return userService.save(requestDto);
    }


}
