package com.internship.task_management_system.services;

import com.internship.task_management_system.entities.User;
import com.internship.task_management_system.exceptions.ResourceAlreadyExistException;
import com.internship.task_management_system.exceptions.ResourceNotFoundException;
import com.internship.task_management_system.jpa.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
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

    public ResponseEntity<User> createUser(User user) {
        checkUniqueUser(user.getUsername());

        User newUser = userRepository.save(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(long id){
        return checkUserExist(id);
    }

    public ResponseEntity<User> updateUser(long id, User updatedUser) {
        checkUserExist(id);
        checkUniqueUser(updatedUser.getUsername());

        updatedUser.setId(id);
        userRepository.save(updatedUser);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build()
                .toUri();
        return ResponseEntity.created(location).build();
    }

    public String deleteUser(long id) {
        checkUserExist(id);
        userRepository.deleteById(id);
        return "User with id " + id + " deleted successfully";
    }

}
