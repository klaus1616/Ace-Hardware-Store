package com.example.AceHardwareStore.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    /**
     * Department id
     */
    private int id;
    /**
     * Department name
     */
    @NotBlank(message = "department name is mandatory")
    @Size(min = 2, max = 50, message =  "department name must be between 2 and 50 characters")
    private String departmentName;

    /**
     * Department to String method
     * @return Department information
     */
    @Override
    public String toString() {
        return "Department number: " + id + ". Department name: " + departmentName;
    }
}
