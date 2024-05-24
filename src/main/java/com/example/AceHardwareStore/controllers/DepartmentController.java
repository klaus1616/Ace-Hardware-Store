package com.example.AceHardwareStore.controllers;

import com.example.AceHardwareStore.daos.DepartmentDao;
import com.example.AceHardwareStore.models.Department;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.List;

/**
 * Handles REST requests to /department/*
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {
    /**
     * Department data access object
     */
    private DepartmentDao departmentDao;

    /**
     * Create new DepartmentController
     *
     * @param departmentDao The department data access object
     */
    public DepartmentController(DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }

    /**
     * Returns a list of all departments
     *
     * @return The list of departments
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public List<Department> listDepartment() {
        return departmentDao.getAllDepartments();
    }

    /**
     * Returns a the department by ID
     *
     * @param id The ID of the department
     *
     * @return The department
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public Department getDepartmentById(@PathVariable int id) {
        return departmentDao.getDepartmentById(id);
    }

    /**
     * Created a new department
     *
     * @param department The department to be created
     *
     * @return The created department
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public Department addDepartment(@Valid @RequestBody Department department) {
        return departmentDao.addDepartment(department);
    }

    /**
     * Deletes a department by id
     *
     * @param id The ID of the department to be deleted
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteDepartment(@PathVariable int id) {
        departmentDao.deleteDepartment(id);
    }
}
