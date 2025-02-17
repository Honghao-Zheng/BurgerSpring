package com.sparta.burgerspring.model.repositories;

import com.sparta.burgerspring.model.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUserName(String username);
}
