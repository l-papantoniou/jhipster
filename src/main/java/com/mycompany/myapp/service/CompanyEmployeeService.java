package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.CompanyEmployee;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link CompanyEmployee}.
 */
public interface CompanyEmployeeService {
    /**
     * Save a companyEmployee.
     *
     * @param companyEmployee the entity to save.
     * @return the persisted entity.
     */
    CompanyEmployee save(CompanyEmployee companyEmployee);

    /**
     * Updates a companyEmployee.
     *
     * @param companyEmployee the entity to update.
     * @return the persisted entity.
     */
    CompanyEmployee update(CompanyEmployee companyEmployee);

    /**
     * Partially updates a companyEmployee.
     *
     * @param companyEmployee the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CompanyEmployee> partialUpdate(CompanyEmployee companyEmployee);

    /**
     * Get all the companyEmployees.
     *
     * @return the list of entities.
     */
    List<CompanyEmployee> findAll();

    /**
     * Get the "id" companyEmployee.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CompanyEmployee> findOne(Long id);

    /**
     * Delete the "id" companyEmployee.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
