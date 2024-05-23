package com.example.AceHardwareStore.models;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    /**
     * Employee id
     */
    private int employeeId;
    /**
     * Employee name
     */
    @NotBlank(message = "Employee name is mandatory")
    @Size(min = 2, max = 255, message =  "Employee name must be between 2 and 255 characters")
    private String name;
    /**
     * Employee hire date
     */
    private LocalDate hireDate;
    /**
     * Employee leave date
     */
    private LocalDate leaveDate;
    /**
     * Employee phone number
     */
    @NotBlank(message = "phone number is mandatory")
    @Size(min = 10, max = 13, message = "phone number must be between 10 and 11 characters")
    private String phoneNumber;
    /**
     * Employee hourly rate
     */
    @DecimalMin(value = "13.00", inclusive = true, message = "Hourly rate must be greater than or equal to $13.00 minimum cook county rate")
    private BigDecimal hourlyRate;
    /**
     * Department id
     */
    @Min(value = 1, message = "Department ID must be greater than 0")
    private int departmentId;

    /**
     *
     * @param employeeId Employee id
     * @param name Employee name
     * @param hireDate Employee hire date
     * @param phoneNumber Employee phone number
     * @param hourlyRate Employee hourly rate
     * @param departmentId department id
     */

    public Employee(int employeeId, String name, LocalDate hireDate,
                    String phoneNumber, BigDecimal hourlyRate, int departmentId) {

        this.employeeId = employeeId;
        this.name = name;
        this.hireDate = hireDate;
        this.phoneNumber = phoneNumber;
        this.hourlyRate = hourlyRate;
        this.departmentId = departmentId;
    }

    /**
     * Helper method that returns an employee without hourly rate.
     * @return Employee
     */
    public Employee withoutHourlyRate() {
        return new Employee(
                this.employeeId,
                this.name,
                this.hireDate,
                this.leaveDate,
                this.phoneNumber,
                this.hourlyRate = null,
                this.departmentId
        );
    }

    /**
     * Employee to String method
     * @return Employee information
     */
    @Override
    public String toString() {
        return "Employee " +
                "id: " + employeeId +
                ",  name: " + name +
                ",  start date: " + hireDate +
                ",  phone number: " + phoneNumber +
                ",  hourly rate: " + hourlyRate +
                ",  department: ";
    }
}
