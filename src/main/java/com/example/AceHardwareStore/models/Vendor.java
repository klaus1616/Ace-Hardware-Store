package com.example.AceHardwareStore.models;

import jakarta.validation.constraints.Min;
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
public class Vendor {
    /**
     * Vendor id
     */
    private int vendorId;
    @NotBlank(message = "Vendor name is mandatory")
    @Size(min = 2, max = 255, message =  "Vendor name must be between 2 and 255 characters")
    private String vendorName;
    /**
     * Vendor contact name
     */
    @NotBlank(message = "Contact name is mandatory")
    @Size(min = 1, max = 255, message =  "Contact name must be between 1 and 255 characters")
    private String contact;
    /**
     * Vendor address
     */
    @NotBlank(message = "Address is mandatory")
    @Size(min = 2, max = 255, message = "Address must be between 2 and 255 characters")
    private String address;
    /**
     * Vendor phone number
     */
    @NotBlank(message = "phone number is mandatory")
    @Size(min = 10, max = 13, message = "phone number must be between 10 and 13 characters")
    private String phoneNumber;
    /**
     * Department id
     */
    @Min(value = 1, message = "Department ID must be greater than 0")
    private int departmentId;

    /**
     * Vendor to String method
     * @return Vendor information
     */

    @Override
    public String toString() {
        return  "Vendor: " + getVendorId() + ". " +
                ", " + getVendorName() +
                ", Address: " + getAddress() +
                ",       Contact: " + getContact() +
                ", " + getPhoneNumber();
    }
}
