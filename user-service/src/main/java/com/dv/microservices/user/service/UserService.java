package com.dv.microservices.user.service;

import org.springframework.stereotype.Service;

import com.dv.microservices.user.dto.UserRequest;
import com.dv.microservices.user.model.User;
import com.dv.microservices.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository; 

    public void saveUser(UserRequest userRequest){
        //map user request to user 
        User user = userRequest.toUser();

        //save user
        userRepository.save(user); 
    }
}
