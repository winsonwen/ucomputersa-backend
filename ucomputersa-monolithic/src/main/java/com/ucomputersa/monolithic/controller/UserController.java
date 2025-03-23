package com.ucomputersa.monolithic.controller;

import com.ucomputersa.monolithic.domain.User;
import com.ucomputersa.monolithic.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/0/users")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/search")
    public ResponseEntity<User> getUsers(@RequestParam String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/search/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUsers(@RequestBody User user) {
        String email = Objects.nonNull(user) && user.getEmail() != null ? user.getEmail() : null;
        if (email != null) {
            return  ResponseEntity.ok(userService.createUser(user));
        }
       return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUsers(@RequestParam String id) {
        if (id == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userService.deleteUser(id));
    }
}
