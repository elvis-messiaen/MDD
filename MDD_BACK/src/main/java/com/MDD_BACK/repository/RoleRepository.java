package com.MDD_BACK.repository;

import com.MDD_BACK.entity.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    boolean existsByName(String name);

    Optional<Role> findByName(String name);
}
