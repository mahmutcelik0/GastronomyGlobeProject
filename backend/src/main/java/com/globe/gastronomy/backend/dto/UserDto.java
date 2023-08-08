package com.globe.gastronomy.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.globe.gastronomy.backend.constants.Gender;
import com.globe.gastronomy.backend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String firstName;

    private String lastName;

    private Gender gender;

    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private Set<RoleDto> roles;
}
