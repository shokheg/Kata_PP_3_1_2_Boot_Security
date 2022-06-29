package ru.kata.spring.boot_security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Optional;

/**
 * @author shokhalevich
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); //аутентификация через email


}
