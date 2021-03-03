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
    public UsersDuplicateCheckResponseDto findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(entity -> {
                    return UsersDuplicateCheckResponseDto.builder()
                            .status(HttpStatus.CONFLICT)
                            .success(false)
                            .build();
                })
                .orElseGet(() -> {
                    return UsersDuplicateCheckResponseDto.builder()
                            .status(HttpStatus.OK)
                            .success(true)
                            .build();
                });
    }

    @Transactional
    public UsersDuplicateCheckResponseDto findByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .map(entity -> {
                    return UsersDuplicateCheckResponseDto.builder()
                            .status(HttpStatus.CONFLICT)
                            .success(false)
                            .build();
                })
                .orElseGet(() -> {
                    return UsersDuplicateCheckResponseDto.builder()
                            .status(HttpStatus.OK)
                            .success(true)
                            .build();
                });
    }

    @Transactional
    public Long save(UserSaveRequestDto requestDto) {
        requestDto.setPassword(
                passwordEncoder.encode(requestDto.getPassword())
        );

        return userRepository.save(requestDto.toEntity()).getId();
    }

}
