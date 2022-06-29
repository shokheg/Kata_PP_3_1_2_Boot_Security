package ru.kata.spring.boot_security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.spring.boot_security.demo.model.Role;

/**
 * @author shokhalevich
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

}
