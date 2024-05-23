package com.example.AceHardwareStore.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * User id
     */
    @Min(value = 5, message = "Username must be at least 5 characters")
    private String username;

    /**
     * User password
     */
    @Min(value = 5, message = "Password must be at least 5 characters")
    private String password;

    /**
     * User phone number
     */
    @NotBlank(message = "phone number is mandatory")
    @Size(min = 10, max = 13, message = "phone number must be between 10 and 11 characters")
    private String phoneNumber;
}
