package com.example.AceHardwareStore.controllers;

import com.example.AceHardwareStore.daos.EmployeeDao;
import com.example.AceHardwareStore.daos.UserDao;
import com.example.AceHardwareStore.models.Employee;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles REST request to /employee/*
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    /**
     * Employee data access object
     */
    private EmployeeDao employeeDao;
    private UserDao userDao;

    /**
     * Creates a new EmployeeController
     *
     * @param employeeDao The employee data access object
     */
    public EmployeeController(EmployeeDao employeeDao, UserDao userDao) {
        this.employeeDao = employeeDao;
        this.userDao = userDao;
    }


    /**
     * Returns a list of all employees
     *
     * @param departmentId Optional id to constrain which employees are returned
     *
     * @return The list of employees
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public List<Employee> listEmployees(
            @RequestParam(required = false) String departmentId, Principal principal
    ) {
        String userName = principal.getName();
        List<String> roles = userDao.getRolesForUser(userName);
        List<Employee> employees;

        if (departmentId != null) {
            employees = employeeDao.getEmployeesByDepartmentId(Integer.parseInt(departmentId));
        } else {
            employees = employeeDao.getAllEmployees();
        }
        if (!roles.contains("ADMIN")) {

            List<Employee> partialEmployeeInformation = new ArrayList<>();

            for (Employee employee: employees) {
                partialEmployeeInformation.add(employee.withoutHourlyRate());
            }
            return partialEmployeeInformation;
        } else {
            return employees;
        }
    }
    /**
     * Returns an employee by their ID
     *
     * @param id The ID of the employee
     *
     * @return The employee
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable int id, Principal principal) {
        String userName = principal.getName();
        List<String> roles = userDao.getRolesForUser(userName);
        Employee employee = employeeDao.getEmployeeById(id);

        if (!roles.contains("ADMIN")) {
            return employee.withoutHourlyRate();
        } else {
            return employee;
        }
    }
    /**
     * Returns employee by their name
     *
     * @param name The name of the employee
     *
     * @return The employees
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/name")
    public Employee getEmployeeByName(@RequestParam String name, Principal principal) {
        String userName = principal.getName();
        List<String> roles = userDao.getRolesForUser(userName);
        Employee employee = employeeDao.getEmployeeByName(name);

        if (!roles.contains("ADMIN")) {
            return employee.withoutHourlyRate();
        } else {
            return employee;
        }
    }
    /**
     * Adds a new employee
     *
     * @param employee The employee to add
     *
     * @return The added employee
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public Employee addEmployee(@Valid @RequestBody Employee employee) {
        return employeeDao.addEmployee(employee);
    }

    /**
     * Updates an employee's details and returns the updated employee
     *
     * @param id The ID of the employee to update
     *
     * @param employee The updated Employee object
     *
     * @return The updated employee
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable int id,@Valid @RequestBody Employee employee) {
        employee.setEmployeeId(id);
        return employeeDao.updateEmployee(employee);
    }

    /**
     * Updates an employee's phone number and returns updated employee
     *
     * @param id The ID of the employee to update
     *
     * @param phoneNumber The updated phone number
     *
     * @return The updated employee
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}/phone_number")
    public Employee updateEmployeePhoneNumber(@PathVariable int id, @RequestBody String phoneNumber) {
        return employeeDao.updateEmployeePhoneNumberById(id, phoneNumber);
    }

    /**
     * Updates an employee's hourly rate and returns updated employee
     *
     * @param id The ID of the employee to update
     *
     * @param hourlyRate The updated employee's hourly rate
     *
     * @return The updated employee
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}/hourly_rate")
    public Employee updateEmployeeHourlyRate(@PathVariable int id, @RequestBody BigDecimal hourlyRate) {
        return employeeDao.updateEmployeeHourlyRateById(id, hourlyRate);
    }
    /**
     * Deletes an employee
     *
     * @param id Id of an employee to be deleted
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable int id) {
        employeeDao.deleteEmployeeById(id);
    }
}
