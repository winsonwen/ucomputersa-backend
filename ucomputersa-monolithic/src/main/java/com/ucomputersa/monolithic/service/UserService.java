package com.ucomputersa.monolithic.service;

import com.ucomputersa.monolithic.domain.User;

public interface UserService {

    User getUserByEmail(String email);

    User deleteUser(String id);
}
