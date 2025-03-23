package com.ucomputersa.monolithic.repository;

import com.ucomputersa.monolithic.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByPhoneNumberAndCountryCode(String phoneNumber, String countryCode);

    Optional<User> findByEmail(String email);
}