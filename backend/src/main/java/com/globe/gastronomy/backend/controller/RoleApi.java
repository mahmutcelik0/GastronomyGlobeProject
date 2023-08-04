package com.globe.gastronomy.backend.controller;

import com.globe.gastronomy.backend.dto.RoleDto;
import com.globe.gastronomy.backend.exception.RoleNotFoundException;
import com.globe.gastronomy.backend.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleApi {
    private final RoleService roleService;

    public RoleApi(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public Set<RoleDto> getAllRoles() throws RoleNotFoundException {
        return roleService.getAllRoles();
    }

    @GetMapping("/findByName")
    public RoleDto getRoleByName(@RequestParam String roleName) {
        return roleService.getRoleByName(roleName);
    }

    @PostMapping
    public ResponseEntity addNewRole(@RequestBody @Valid RoleDto roleDto){
        return roleService.addNewRole(roleDto);
    }
}