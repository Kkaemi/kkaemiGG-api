package com.spring.kkaemiGG.service;

import com.spring.kkaemiGG.domain.user.User;
import com.spring.kkaemiGG.domain.user.UserRepository;
import com.spring.kkaemiGG.exception.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }

    public User getPostsFetchedUser(Long userId) throws InternalServerErrorException {
        return userRepository.fetchPostsById(userId)
                .orElseThrow(() -> new InternalServerErrorException("해당 아이디의 유저를 찾을 수 없습니다."));
    }
}
