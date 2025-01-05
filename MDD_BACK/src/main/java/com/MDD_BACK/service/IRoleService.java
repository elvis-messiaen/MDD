package com.MDD_BACK.service;

import com.MDD_BACK.entity.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleService {

    Role create(Role role);

    Optional<Role> findById(Long id);

    List<Role> findAll();

    Role update(Long id, Role role);

    void deleteById(Long id);
}