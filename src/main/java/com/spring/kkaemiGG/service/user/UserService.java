package com.spring.kkaemiGG.service.user;

import com.spring.kkaemiGG.domain.user.UserRepository;
import com.spring.kkaemiGG.web.dto.user.UserSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long save(UserSaveRequestDto requestDto) {
        requestDto.setPassword(
                passwordEncoder.encode(requestDto.getPassword())
        );

        return userRepository.save(requestDto.toEntity()).getId();
    }

}
