package com.ucomputersa.monolithic.service.impl;

import com.ucomputersa.monolithic.constant.RoleEnum;
import com.ucomputersa.monolithic.domain.User;
import com.ucomputersa.monolithic.repository.UserRepository;
import com.ucomputersa.monolithic.security.JwtService;
import com.ucomputersa.monolithic.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private JwtService jwtService;

    private UserRepository userRepository;

    private MongoTemplate mongoTemplate;


    @Override
    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }

    @Override
    public User deleteUser(String id) {
        try{
            userRepository.deleteById(id);
        } catch (Exception e){
            LOGGER.error("Error in deleting user", e);
            throw e;
        }

        LOGGER.info("User deleted with id: " + id);
        return null;
    }

    private User convertToRegularCustomer(GoogleIdToken.Payload payload) {
        return User.builder()
                .lastName(payload.get("family_name") + "")
                .firstName(payload.get("given_name") + "")
                .email(payload.getEmail() + "")
                .roles(List.of(RoleEnum.REGULAR_USER))
                .build();
//           TODO     .avatar(memberAvatar.get().getUrl())
    }

    public User createUser(User user) {
        User userResult = null;
        try{
            user.setRoles(Arrays.asList(RoleEnum.REGULAR_USER));
            userResult = userRepository.save(user);
        } catch (Exception e){
            LOGGER.error("Error in creating user", e);
            throw e;
        }
        return userResult;
    }

    public List<User> getUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .peek(user -> user.setRoles(Collections.singletonList(RoleEnum.REGULAR_USER)))
                .collect(Collectors.toList());
    }

    public User getUserById(String userId) {
        User currentUser = userRepository.findById(userId).orElse(null);
        if (currentUser == null) {
            return null;
        }
        currentUser.setRoles(Collections.singletonList(RoleEnum.REGULAR_USER));
        return currentUser;
    }
}
