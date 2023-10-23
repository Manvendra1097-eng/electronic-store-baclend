package com.m2code.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.m2code.entities.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(value = {"password"}, allowSetters = true)
public class UserDto {
    private String userId;
    @NotBlank(message = "name is required")
    private String name;
    @Email(message = "invalid user email")
    private String email;
    //    @Pattern(regexp = ,message = "")
    @NotEmpty(message = "password is required")
    private String password;
    private String gender;
    private String about;
    private String profileImage;
    private Set<Role> roles = new HashSet<>();

}
