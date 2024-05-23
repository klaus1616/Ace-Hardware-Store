package com.example.AceHardwareStore.daos;

import com.example.AceHardwareStore.exceptions.DaoException;
import com.example.AceHardwareStore.models.Employee;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Component
public class EmployeeDao extends Employee {
    /**
     * JDBC template instance
     */
    private JdbcTemplate jdbcTemplate;

    /**
     * Created new EmployeeDao
     *
     * @param dataSource The datasource to connect to
     */
    public EmployeeDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Returns a list of all employees
     *
     * @return The list of Employees
     * @throws DaoException If an error occurs
     */
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employee ORDER BY employee_id;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
            while (rowSet.next()) {
                Employee employee = mapRowToEmployee(rowSet);
                employees.add(employee);
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return employees;
    }

    /**
     * Returns an employee by their id
     *
     * @param id The id of the employee
     * @return The employee object
     * @throws DaoException If an error occurs
     */
    public Employee getEmployeeById(int id) {
        Employee employee = null;
        String sql = "SELECT * FROM employee WHERE employee_id = ?;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
            if (rowSet.next()) {
                return mapRowToEmployee(rowSet);
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        }
        return null;
    }

    /**
     * Returns a list of all employees by their department id
     *
     * @param departmentId The id of the department
     * @return The List of employees
     * @throws DaoException If an error occurs
     */
    public List<Employee> getEmployeesByDepartmentId(int departmentId) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employee WHERE department_id = ? ORDER BY name ASC;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, departmentId);
            while (rowSet.next()) {
                Employee employee = mapRowToEmployee(rowSet);
                employees.add(employee);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        }
        return employees;
    }

    /**
     * Returns an employee by their name
     *
     * @param employeeName The name of the employee
     * @return The employee
     * @throws DaoException If an error occurs
     */
    public Employee getEmployeeByName(String employeeName) {
        Employee employee = null;
        String sql = "SELECT * FROM employee WHERE name ILIKE ?;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, "%" + employeeName + "%");
            while (rowSet.next()) {
                return mapRowToEmployee(rowSet);
            }
        }
        catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database");
        }
        return null;
    }
    /**
     * Creates a new employees
     *
     * @param employee The employee to be created
     * @return The created employee object with the generated id or null id creation failed
     * @throws DaoException If an error occurs
     */
    public Employee addEmployee(Employee employee) {
        Employee newEmployee = null;
        String sql = "INSERT INTO employee(name, hire_date, phone_number, hourly_rate, department_id) VALUES (?, ?, ?, ?, ?) RETURNING employee_id;";
        try {
            Integer newEmployeeId = jdbcTemplate.queryForObject(sql, int.class, employee.getName(), employee.getHireDate(),
                    employee.getPhoneNumber(), employee.getHourlyRate(), employee.getDepartmentId());
            if (newEmployeeId != null) {
                return getEmployeeById(newEmployeeId);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }
        return null;
    }

    /**
     * Deletes an employee by their id
     *
     * @param employeeId The id of the employee to be deleted
     * @throws DaoException If an error occurs
     */
    public void deleteEmployeeById(int employeeId) {

        String sql = "DELETE FROM employee WHERE employee_id = ?";
        try {
            jdbcTemplate.update(sql, employeeId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation");
        }
    }

    /**
     * Updates an existing employee
     *
     * @param updatedEmployee The employee object to be updated
     * @return The updated employee object
     * @throws DaoException If an error occurs
     */
    public Employee updateEmployee(Employee updatedEmployee) {
        Employee updatedEmployeeResult = null;
        String sql =
                "UPDATE employee SET name = ?, hire_date = ?, phone_number = ?," +
                        " hourly_rate = ?, department_id = ? WHERE employee_id = ?";

        try {
            int numberOfRows = jdbcTemplate.update(
                    sql,
                    updatedEmployee.getName(),
                    updatedEmployee.getHireDate(),
                    updatedEmployee.getPhoneNumber(),
                    updatedEmployee.getHourlyRate(),
                    updatedEmployee.getDepartmentId(),
                    updatedEmployee.getEmployeeId()
            );
            if (numberOfRows == 0) {
                throw new DaoException("Zero rows affected");
            } else {
                updatedEmployeeResult = getEmployeeById(updatedEmployee.getEmployeeId());
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }

        return updatedEmployeeResult;
    }

    /**
     * Updates an existing employees phone number by id
     *
     * @param employeeId The id of the employee to update
     * @param newPhoneNumber The new phone number to set
     * @return The updated Employee object
     * @throws DaoException If an error occurs
     */
    public Employee updateEmployeePhoneNumberById(int employeeId, String newPhoneNumber) {
        Employee updatePhoneNumber = null;
        String sql = "UPDATE employee SET phone_number = ? WHERE employee_id = ?";
        try {
            int numberOfRows = jdbcTemplate.update(sql, newPhoneNumber, employeeId);
            if (numberOfRows == 0) {
                throw new DaoException("zero rows affected");
            } else {
                updatePhoneNumber = (Employee)getEmployeeById(employeeId);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }
        return updatePhoneNumber;
    }

    /**
     * Updates the hourly rate of an employee by thir id
     *
     * @param employeeId The id of the employee to update
     * @param newHourlyRate The new hourly rate to set
     * @return The updated employee object
     * @throws DaoException If an error occurs
     */
    public Employee updateEmployeeHourlyRateById(int employeeId, BigDecimal newHourlyRate) {
        Employee updateHourlyRate = null;
        String sql = "UPDATE employee SET hourly_rate = ? WHERE employee_id = ?";
        try {
            int numberOfRows = jdbcTemplate.update(sql, newHourlyRate, employeeId);
            if (numberOfRows == 0) {
                throw new DaoException("zero rows affected");
            } else {
                updateHourlyRate = getEmployeeById(employeeId);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }
        return updateHourlyRate;
    }
    /**
     * Maps a row in the result set to employee
     *
     * @param rowSet The SqlRowSet
     * @return The employee object mapped from the row set
     */
    public Employee mapRowToEmployee(SqlRowSet rowSet) {
        Employee employee = new Employee();
        employee.setEmployeeId(rowSet.getInt("employee_id"));
        employee.setName(rowSet.getString("name"));
        employee.setHireDate(rowSet.getDate("hire_date").toLocalDate());
        if (rowSet.getDate("leave_date") != null) {
            employee.setLeaveDate(rowSet.getDate("leave_date").toLocalDate());
        }
        employee.setPhoneNumber(rowSet.getString("phone_number"));
        employee.setHourlyRate(rowSet.getBigDecimal("hourly_rate"));
        employee.setDepartmentId(rowSet.getInt("department_id"));
        return employee;
    }
}
