package com.example.AceHardwareStore.controllers;

import com.example.AceHardwareStore.daos.VendorDao;
import com.example.AceHardwareStore.models.Vendor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles REST request to /vendor/*
 */
@RestController
@RequestMapping("/vendor")
public class VendorController {
    /**
     * Vendor data access object
     */
    private VendorDao vendorDao;

    /**
     * Creates a new VendorController
     * @param vendorDao The vendor data access object
     */
    public VendorController(VendorDao vendorDao) {
        this.vendorDao = vendorDao;
    }

    /**
     * Returns a list of all vendors
     *
     * @return The list of vendors
     */
    @PreAuthorize("permitAll")
    @GetMapping("")
    public List<Vendor> listVendors() {
        return vendorDao.getAllVendors();
    }

    /**
     * Returns the vendor by ID
     *
     * @param id The ID of the vendor
     *
     * @return The vendor
     */
    @PreAuthorize("permitAll")
    @GetMapping("/{id}")
    public Vendor getVendorById(@PathVariable int id) {
        return vendorDao.getVendorById(id);
    }

    /**
     * Adds a new vendor
     *
     * @param vendor The vendor to add
     *
     * @return The added vendor
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public Vendor addVendor(@Valid @RequestBody Vendor vendor) {
        return vendorDao.addVendor(vendor);
    }

    /**
     * Updates a vendor's details and returns the updated vendor
     *
     * @param id The ID of the vendor to be updated
     *
     * @param vendor The updated Vendor object
     *
     * @return The updated vendor
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public Vendor updateVendor (@PathVariable int id, @Valid @RequestBody Vendor vendor) {
        vendor.setVendorId(id);
        return vendorDao.updateVendor(vendor);
    }

    /**
     *  Deletes the vendor by ID
     * @param id The ID of vendor to be deleted
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteVendor(@PathVariable int id) {
        vendorDao.deleteVendorById(id);
    }
}
