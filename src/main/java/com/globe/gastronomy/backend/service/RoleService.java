package com.globe.gastronomy.backend.service;

import com.globe.gastronomy.backend.dto.RoleDto;
import com.globe.gastronomy.backend.dto.RoleDtoPopulator;
import com.globe.gastronomy.backend.dto.UserDto;
import com.globe.gastronomy.backend.exception.RoleNotFoundException;
import com.globe.gastronomy.backend.model.Role;
import com.globe.gastronomy.backend.model.User;
import com.globe.gastronomy.backend.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Set<RoleDto> getAllRoles() throws RoleNotFoundException {
        List<Role> rolesList = roleRepository.findAll();

        Set<RoleDto> roles = rolesList.stream()
                .map(e -> new RoleDtoPopulator().populate(e))
                .collect(Collectors.toSet());

        if (roles.isEmpty()) throw new RoleNotFoundException("Role Not Found!");

        return roles;
    }

    public RoleDto getRoleByName(String roleName) {
        return new RoleDtoPopulator().populate(findRoleByRoleName(roleName));
    }

    public ResponseEntity addNewRole(RoleDto roleDto) {
        if (!roleDto.getRoleName().startsWith("ROLE_")) {
            roleDto.setRoleName("ROLE_" + roleDto.getRoleName());
        }

        Optional<Role> role = roleRepository.findRoleByRoleName(roleDto.getRoleName());

        if (role.isEmpty()) {
            roleRepository.save(new RoleDtoPopulator().reverseConverter(roleDto));
            return new ResponseEntity<>("Role Saved Successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Role Already Exists", HttpStatus.CONFLICT);
        }
    }

    public void setUserRoles(User newUser, UserDto userDto) throws RoleNotFoundException {
        if (userDto.getRoles().isEmpty()) throw new RoleNotFoundException("Role Not Found");
        newUser.setRoles(null); //CLEAN
        userDto.getRoles().forEach(e -> {
            String role = rolePrefixController(e.getRoleName());
            if (newUser.getRoles() == null) {
                newUser.setRoles(new HashSet<>());
            }
            newUser.getRoles().add(roleRepository.findRoleByRoleName(role).orElseThrow());
        });
    }

    public ResponseEntity updateExistRole(String existRoleName, RoleDto newRole) {
        String existRole = rolePrefixController(existRoleName);
        Optional<Role> role = roleRepository.findRoleByRoleName(existRole);

        if (role.isEmpty()) return new ResponseEntity("Role does not exist!", HttpStatus.INTERNAL_SERVER_ERROR);

        String newRoleName = rolePrefixController(newRole.getRoleName());

        roleRepository.updateRoleByRoleName(existRole, newRoleName);

        return new ResponseEntity("Role Updated", HttpStatus.OK);
    }


    public ResponseEntity deleteRole(String roleName) {
        String existRole = rolePrefixController(roleName);
        Optional<Role> role = roleRepository.findRoleByRoleName(existRole);

        if (role.isEmpty()) return new ResponseEntity("Role does not exist!", HttpStatus.INTERNAL_SERVER_ERROR);

        roleRepository.delete(role.get());

        return new ResponseEntity("Role deleted", HttpStatus.OK);
    }

    protected Role findRoleByRoleName(String roleName){
        if (!roleName.startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName;
        }

        Optional<Role> role = roleRepository.findRoleByRoleName(roleName);

        return role.orElseThrow();
    }

    private String rolePrefixController(String roleName) {
        return roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName;
    }
}
