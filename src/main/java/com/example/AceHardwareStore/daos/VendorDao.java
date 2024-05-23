package com.example.AceHardwareStore.daos;

import com.example.AceHardwareStore.exceptions.DaoException;
import com.example.AceHardwareStore.models.Vendor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
@Component
public class VendorDao {
    /**
     * JDBC template instance
     */
    private JdbcTemplate jdbcTemplate;

    /**
     * Creates ne VendorDao
     *
     * @param dataSource The datasource to connect to
     */
    public VendorDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Returns a list of all Vendors
     *
     * @return The list of Vendors
     * @throws DaoException If an error occurs
     */
    public List<Vendor> getAllVendors() {
        List<Vendor> vendors = new ArrayList<>();
        String sql = "SELECT * FROM vendor;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
            while (rowSet.next()) {
                Vendor vendor = mapRowToVendor(rowSet);
                vendors.add(vendor);
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        }
        return vendors;
    }
    /**
     * Returns a Vendor by their id
     *
     * @param id The id of the Vendor
     * @return The Vendor object
     * @throws DaoException If an error occurs
     */
    public Vendor getVendorById(int id) {
        Vendor vendor = null;
        String sql = "SELECT * FROM vendor WHERE vendor_id = ?;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
            if (rowSet.next()) {
                vendor = mapRowToVendor(rowSet);
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        }
        return vendor;
    }

    /**
     * Creates a new Vendor
     *
     * @param vendor The Vendor to be created
     * @return The created Vendor object with the generated id
     * @throws DaoException If an error occurs
     */
    public Vendor addVendor(Vendor vendor) {
        Vendor newVendor = null;
        String sql = "INSERT INTO vendor(vendor_name, contact_name, address, phone_number, department_id) VALUES (?, ?, ?, ?, ?) RETURNING vendor_id;";
        try {
            int newVendorId = jdbcTemplate.queryForObject(sql, int.class, vendor.getVendorName(), vendor.getContact(),
                    vendor.getAddress(), vendor.getPhoneNumber(), vendor.getDepartmentId());
            newVendor = getVendorById(newVendorId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }
        return newVendor;
    }

    /**
     * Updated an existing Vendor
     *
     * @param updatedVendor The Vendor object to be updated
     * @return The updated Vendor object
     * @throws DaoException If an error occurs
     */
    public Vendor updateVendor(Vendor updatedVendor) {
        Vendor updatedVendorResult = null;
        String sql =
                "UPDATE vendor SET vendor_name = ?, contact_name = ?, address = ?," +
                        "phone_number = ?, department_id = ? WHERE vendor_id = ?;";
        try {
            int numberOfRows = jdbcTemplate.update(
                    sql,
                    updatedVendor.getVendorName(),
                    updatedVendor.getContact(),
                    updatedVendor.getAddress(),
                    updatedVendor.getPhoneNumber(),
                    updatedVendor.getDepartmentId(),
                    updatedVendor.getVendorId()
            );
            if (numberOfRows == 0) {
                throw new DaoException("Zero rows affected");
            } else {
                updatedVendorResult = getVendorById(updatedVendor.getVendorId());
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }
        return updatedVendor;
    }

    /**
     * Deletes an Vendor by their id
     *
     * @param vendorId The id of the Vendor to be deleted
     * @return Number of rows affected
     * @throws DaoException If an error occurs
     */
    public int deleteVendorById(int vendorId) {
        int numberOfRows = 0;
        String sql = "DELETE FROM vendor WHERE vendor_id = ?";
        try {
            numberOfRows = jdbcTemplate.update(sql, vendorId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation");
        }
        return numberOfRows;
    }

    /**
     * Maps a row in the result set to vendor
     *
     * @param rowSet The SqlRowSet
     * @return The vendor object mapped from the row set
     */
    public Vendor mapRowToVendor(SqlRowSet rowSet) {
        Vendor vendor = new Vendor();
        vendor.setVendorId(rowSet.getInt("vendor_id"));
        vendor.setVendorName(rowSet.getString("vendor_name"));
        vendor.setContact(rowSet.getString("contact_name"));
        vendor.setAddress(rowSet.getString("address"));
        vendor.setPhoneNumber(rowSet.getString("phone_number"));
        vendor.setDepartmentId(rowSet.getInt("department_id"));
        return vendor;
    }
}
