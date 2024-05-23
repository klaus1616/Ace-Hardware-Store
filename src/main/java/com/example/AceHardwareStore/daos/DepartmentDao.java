package com.example.AceHardwareStore.daos;

import com.example.AceHardwareStore.exceptions.DaoException;
import com.example.AceHardwareStore.models.Department;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
@Component
public class DepartmentDao {
    /**
     * JDBC template instance
     */
    private JdbcTemplate jdbcTemplate;

    /**
     * Creates new DepartmentDao
     *
     * @param dataSource The datasource to connect to
     */
    public DepartmentDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Returns a list of all departments
     *
     * @return The list of departments
     * @throws DaoException If an error occurs
     */
    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM department;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        while (rowSet.next()) {
            departments.add(mapRowToDepartment(rowSet));
        }
        return departments;
    }
    /**
     * Returns a department by by their id
     *
     * @param id The id of the department
     * @return The department
     * @throws DaoException If an error occurs
     */
    public Department getDepartmentById( int id) {
        String sql = "SELECT * FROM department WHERE department_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (rowSet.next()) {
            return mapRowToDepartment(rowSet);
        } else {
            return null;
        }
    }

    /**
     * Creates a new department and returns the created department with the generated id
     *
     * @param department Department to be created
     * @return The created department object with the generated department id
     * @throws DaoException If an error occurs
     */
    public Department addDepartment(Department department) {
        Department newDepartment = null;
        String sql = "INSERT INTO department (department_name) VALUES (?) RETURNING department_id;";
        try {
            int newDepartmentId = jdbcTemplate.queryForObject(sql, int.class, department.getDepartmentName());
            newDepartment = getDepartmentById(newDepartmentId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }
        return newDepartment;
    }

    /**
     * Deletes a department by their id
     *
     * @param department_id The id of department
     * @return The number of affected rows
     * @throws DaoException If an error occurs
     */
    public int deleteDepartment(int department_id) {
        int numberOfRows = 0;
        String deleteEmployeeSql = "DELETE FROM employee WHERE department_id = ?;";
        String deleteVendorSql = "DELETE FROM vendor WHERE department_id = ?;";
        String deleteDepartmentSql = "DELETE FROM department WHERE department_id = ?";
        try {
            jdbcTemplate.update(deleteEmployeeSql, department_id);
            jdbcTemplate.update(deleteVendorSql, department_id);
            numberOfRows = jdbcTemplate.update(deleteDepartmentSql, department_id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation");
        }
        return numberOfRows;
    }

    /**
     * Maps a row in the result set to department
     *
     * @param rowSet The SqlRowSet
     * @return The department object mapped from the row set
     */

    public Department mapRowToDepartment(SqlRowSet rowSet) {
        Department department = new Department();
        department.setId(rowSet.getInt("department_id"));
        department.setDepartmentName(rowSet.getString("department_name"));
        return department;
    }
}
