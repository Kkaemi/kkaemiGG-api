package com.spring.kkaemiGG.service.user;

import com.spring.kkaemiGG.domain.user.UserRepository;
import com.spring.kkaemiGG.web.dto.user.UserSaveRequestDto;
import com.spring.kkaemiGG.web.dto.user.UsersDuplicateCheckResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long save(UserSaveRequestDto requestDto) {

        requestDto.setPassword(
                passwordEncoder.encode(requestDto.getPassword())
        );

        return userRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public UsersDuplicateCheckResponseDto findByType(String type, String value) {

        if (type.equals("email")) {
            return userRepository.findByEmail(value)
                    .map(entity -> UsersDuplicateCheckResponseDto.builder()
                            .status(HttpStatus.CONFLICT.value())
                            .message("EXIST")
                            .build())
                    .orElseGet(() -> UsersDuplicateCheckResponseDto.builder()
                            .status(HttpStatus.OK.value())
                            .message("NOT_EXIST")
                            .build());
        }

        return userRepository.findByNickname(value)
                .map(entity -> UsersDuplicateCheckResponseDto.builder()
                        .status(HttpStatus.CONFLICT.value())
                        .message("EXIST")
                        .build())
                .orElseGet(() -> UsersDuplicateCheckResponseDto.builder()
                        .status(HttpStatus.OK.value())
                        .message("NOT_EXIST")
                        .build());

    }
}
