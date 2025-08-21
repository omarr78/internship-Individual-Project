package com.internship.task_management_system.services;

import com.internship.task_management_system.dto.user.UserRequestDto;
import com.internship.task_management_system.dto.user.UserResponseDto;
import com.internship.task_management_system.entities.User;
import com.internship.task_management_system.exceptions.ResourceAlreadyExistException;
import com.internship.task_management_system.exceptions.ResourceNotFoundException;
import com.internship.task_management_system.jpa.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    // helper methods

    public User checkUserExist(long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User with id " + id + " Not found")
        );
    }

    void checkUniqueUser(String username) {
        Optional<User> newUser = userRepository.findByUsername(username);
        if (newUser.isPresent()) {
            throw new ResourceAlreadyExistException("username : " + username + " already exist");
        }
    }

    // methods

    public UserResponseDto register(UserRequestDto user) {
        String password = user.getPassword();
        User newUser = new User(user);

        checkUniqueUser(newUser.getUsername());
        newUser.setPassword(passwordEncoder.encode(password));

        User returnedUser = userRepository.save(newUser);

        return new UserResponseDto(returnedUser);
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserResponseDto::new).toList();
    }

    public UserResponseDto getUser(long id){
        return new UserResponseDto(checkUserExist(id));
    }

    public void deleteUser(long id) {
        checkUserExist(id);
        userRepository.deleteById(id);
    }
}
