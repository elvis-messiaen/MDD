package com.MDD_BACK.service.impl;
import com.MDD_BACK.dto.RoleDTO;
import com.MDD_BACK.entity.Utilisateur;
import com.MDD_BACK.repository.UtilisateurRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import com.MDD_BACK.entity.Role;
import com.MDD_BACK.repository.RoleRepository;

    @Service
    public class RoleServiceImpl {

        @Autowired
        private RoleRepository roleRepository;

        @Autowired
        private UtilisateurRepository utilisateurRepository;

        @Autowired
        private ModelMapper modelMapper;

        public List<RoleDTO> getAllRoles() {
            return StreamSupport.stream(roleRepository.findAll().spliterator(), false)
                    .map(role -> modelMapper.map(role, RoleDTO.class))
                    .collect(Collectors.toList());
        }

        public RoleDTO getRoleById(Long id) {
            Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
            return modelMapper.map(role, RoleDTO.class);
        }

        public RoleDTO saveRole(RoleDTO roleDTO) {
            Role role = modelMapper.map(roleDTO, Role.class);
            role = roleRepository.save(role);
            return modelMapper.map(role, RoleDTO.class);
        }

        public RoleDTO updateRole(Long id, RoleDTO roleDTO) {
            if (roleRepository.existsById(id)) {
                Role role = modelMapper.map(roleDTO, Role.class);
                role.setId(id);
                role = roleRepository.save(role);
                return modelMapper.map(role, RoleDTO.class);
            } else {
                throw new RuntimeException("Role not found");
            }
        }

        public void deleteRole(Long id) {
            if (roleRepository.existsById(id)) {
                dissociateUsersFromRole(id);
                roleRepository.deleteById(id);
            } else {
                throw new RuntimeException("Role not found");
            }
        }

        public void dissociateUsersFromRole(Long roleId) {
            List<Utilisateur> users = (List<Utilisateur>) utilisateurRepository.findAll();
            for (Utilisateur user : users) {
                user.getRole().removeIf(role -> role.getId() == roleId);
                utilisateurRepository.save(user);
            }
        }
    }