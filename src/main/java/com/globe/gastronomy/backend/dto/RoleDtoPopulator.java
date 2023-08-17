package com.globe.gastronomy.backend.dto;

import com.globe.gastronomy.backend.model.Role;

public class RoleDtoPopulator extends AbstractPopulator<Role,RoleDto>{

    @Override
    protected RoleDto populate(Role role, RoleDto roleDto) {
        roleDto.setRoleName(role.getRoleName());

        return roleDto;
    }

    @Override
    protected Role reverseConverter(RoleDto roleDto, Role role) {
        role.setRoleName(roleDto.getRoleName());

        return role;
    }

    @Override
    public RoleDto generateTarget() {
        return new RoleDto();
    }

    @Override
    public Role generateSource() {
        return new Role();
    }
}
