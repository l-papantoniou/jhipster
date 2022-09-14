package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Company;
import com.mycompany.myapp.domain.CompanyEmployee;
import com.mycompany.myapp.domain.Employee;
import com.mycompany.myapp.repository.CompanyEmployeeRepository;
import com.mycompany.myapp.service.criteria.CompanyEmployeeCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CompanyEmployeeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompanyEmployeeResourceIT {

    private static final String ENTITY_API_URL = "/api/company-employees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompanyEmployeeRepository companyEmployeeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompanyEmployeeMockMvc;

    private CompanyEmployee companyEmployee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyEmployee createEntity(EntityManager em) {
        CompanyEmployee companyEmployee = new CompanyEmployee();
        return companyEmployee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyEmployee createUpdatedEntity(EntityManager em) {
        CompanyEmployee companyEmployee = new CompanyEmployee();
        return companyEmployee;
    }

    @BeforeEach
    public void initTest() {
        companyEmployee = createEntity(em);
    }

    @Test
    @Transactional
    void createCompanyEmployee() throws Exception {
        int databaseSizeBeforeCreate = companyEmployeeRepository.findAll().size();
        // Create the CompanyEmployee
        restCompanyEmployeeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyEmployee))
            )
            .andExpect(status().isCreated());

        // Validate the CompanyEmployee in the database
        List<CompanyEmployee> companyEmployeeList = companyEmployeeRepository.findAll();
        assertThat(companyEmployeeList).hasSize(databaseSizeBeforeCreate + 1);
        CompanyEmployee testCompanyEmployee = companyEmployeeList.get(companyEmployeeList.size() - 1);
    }

    @Test
    @Transactional
    void createCompanyEmployeeWithExistingId() throws Exception {
        // Create the CompanyEmployee with an existing ID
        companyEmployee.setId(1L);

        int databaseSizeBeforeCreate = companyEmployeeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyEmployeeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyEmployee))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyEmployee in the database
        List<CompanyEmployee> companyEmployeeList = companyEmployeeRepository.findAll();
        assertThat(companyEmployeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCompanyEmployees() throws Exception {
        // Initialize the database
        companyEmployeeRepository.saveAndFlush(companyEmployee);

        // Get all the companyEmployeeList
        restCompanyEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyEmployee.getId().intValue())));
    }

    @Test
    @Transactional
    void getCompanyEmployee() throws Exception {
        // Initialize the database
        companyEmployeeRepository.saveAndFlush(companyEmployee);

        // Get the companyEmployee
        restCompanyEmployeeMockMvc
            .perform(get(ENTITY_API_URL_ID, companyEmployee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(companyEmployee.getId().intValue()));
    }

    @Test
    @Transactional
    void getCompanyEmployeesByIdFiltering() throws Exception {
        // Initialize the database
        companyEmployeeRepository.saveAndFlush(companyEmployee);

        Long id = companyEmployee.getId();

        defaultCompanyEmployeeShouldBeFound("id.equals=" + id);
        defaultCompanyEmployeeShouldNotBeFound("id.notEquals=" + id);

        defaultCompanyEmployeeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCompanyEmployeeShouldNotBeFound("id.greaterThan=" + id);

        defaultCompanyEmployeeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCompanyEmployeeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCompanyEmployeesByCompanyIsEqualToSomething() throws Exception {
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            companyEmployeeRepository.saveAndFlush(companyEmployee);
            company = CompanyResourceIT.createEntity(em);
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        em.persist(company);
        em.flush();
        companyEmployee.setCompany(company);
        companyEmployeeRepository.saveAndFlush(companyEmployee);
        Long companyId = company.getId();

        // Get all the companyEmployeeList where company equals to companyId
        defaultCompanyEmployeeShouldBeFound("companyId.equals=" + companyId);

        // Get all the companyEmployeeList where company equals to (companyId + 1)
        defaultCompanyEmployeeShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }

    @Test
    @Transactional
    void getAllCompanyEmployeesByEmployeeIsEqualToSomething() throws Exception {
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            companyEmployeeRepository.saveAndFlush(companyEmployee);
            employee = EmployeeResourceIT.createEntity(em);
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        em.persist(employee);
        em.flush();
        companyEmployee.setEmployee(employee);
        companyEmployeeRepository.saveAndFlush(companyEmployee);
        Long employeeId = employee.getId();

        // Get all the companyEmployeeList where employee equals to employeeId
        defaultCompanyEmployeeShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the companyEmployeeList where employee equals to (employeeId + 1)
        defaultCompanyEmployeeShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCompanyEmployeeShouldBeFound(String filter) throws Exception {
        restCompanyEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyEmployee.getId().intValue())));

        // Check, that the count call also returns 1
        restCompanyEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCompanyEmployeeShouldNotBeFound(String filter) throws Exception {
        restCompanyEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCompanyEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCompanyEmployee() throws Exception {
        // Get the companyEmployee
        restCompanyEmployeeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCompanyEmployee() throws Exception {
        // Initialize the database
        companyEmployeeRepository.saveAndFlush(companyEmployee);

        int databaseSizeBeforeUpdate = companyEmployeeRepository.findAll().size();

        // Update the companyEmployee
        CompanyEmployee updatedCompanyEmployee = companyEmployeeRepository.findById(companyEmployee.getId()).get();
        // Disconnect from session so that the updates on updatedCompanyEmployee are not directly saved in db
        em.detach(updatedCompanyEmployee);

        restCompanyEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCompanyEmployee.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCompanyEmployee))
            )
            .andExpect(status().isOk());

        // Validate the CompanyEmployee in the database
        List<CompanyEmployee> companyEmployeeList = companyEmployeeRepository.findAll();
        assertThat(companyEmployeeList).hasSize(databaseSizeBeforeUpdate);
        CompanyEmployee testCompanyEmployee = companyEmployeeList.get(companyEmployeeList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingCompanyEmployee() throws Exception {
        int databaseSizeBeforeUpdate = companyEmployeeRepository.findAll().size();
        companyEmployee.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, companyEmployee.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyEmployee))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyEmployee in the database
        List<CompanyEmployee> companyEmployeeList = companyEmployeeRepository.findAll();
        assertThat(companyEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompanyEmployee() throws Exception {
        int databaseSizeBeforeUpdate = companyEmployeeRepository.findAll().size();
        companyEmployee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyEmployee))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyEmployee in the database
        List<CompanyEmployee> companyEmployeeList = companyEmployeeRepository.findAll();
        assertThat(companyEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompanyEmployee() throws Exception {
        int databaseSizeBeforeUpdate = companyEmployeeRepository.findAll().size();
        companyEmployee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyEmployee))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompanyEmployee in the database
        List<CompanyEmployee> companyEmployeeList = companyEmployeeRepository.findAll();
        assertThat(companyEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompanyEmployeeWithPatch() throws Exception {
        // Initialize the database
        companyEmployeeRepository.saveAndFlush(companyEmployee);

        int databaseSizeBeforeUpdate = companyEmployeeRepository.findAll().size();

        // Update the companyEmployee using partial update
        CompanyEmployee partialUpdatedCompanyEmployee = new CompanyEmployee();
        partialUpdatedCompanyEmployee.setId(companyEmployee.getId());

        restCompanyEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanyEmployee.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompanyEmployee))
            )
            .andExpect(status().isOk());

        // Validate the CompanyEmployee in the database
        List<CompanyEmployee> companyEmployeeList = companyEmployeeRepository.findAll();
        assertThat(companyEmployeeList).hasSize(databaseSizeBeforeUpdate);
        CompanyEmployee testCompanyEmployee = companyEmployeeList.get(companyEmployeeList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateCompanyEmployeeWithPatch() throws Exception {
        // Initialize the database
        companyEmployeeRepository.saveAndFlush(companyEmployee);

        int databaseSizeBeforeUpdate = companyEmployeeRepository.findAll().size();

        // Update the companyEmployee using partial update
        CompanyEmployee partialUpdatedCompanyEmployee = new CompanyEmployee();
        partialUpdatedCompanyEmployee.setId(companyEmployee.getId());

        restCompanyEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanyEmployee.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompanyEmployee))
            )
            .andExpect(status().isOk());

        // Validate the CompanyEmployee in the database
        List<CompanyEmployee> companyEmployeeList = companyEmployeeRepository.findAll();
        assertThat(companyEmployeeList).hasSize(databaseSizeBeforeUpdate);
        CompanyEmployee testCompanyEmployee = companyEmployeeList.get(companyEmployeeList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingCompanyEmployee() throws Exception {
        int databaseSizeBeforeUpdate = companyEmployeeRepository.findAll().size();
        companyEmployee.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, companyEmployee.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companyEmployee))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyEmployee in the database
        List<CompanyEmployee> companyEmployeeList = companyEmployeeRepository.findAll();
        assertThat(companyEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompanyEmployee() throws Exception {
        int databaseSizeBeforeUpdate = companyEmployeeRepository.findAll().size();
        companyEmployee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companyEmployee))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyEmployee in the database
        List<CompanyEmployee> companyEmployeeList = companyEmployeeRepository.findAll();
        assertThat(companyEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompanyEmployee() throws Exception {
        int databaseSizeBeforeUpdate = companyEmployeeRepository.findAll().size();
        companyEmployee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companyEmployee))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompanyEmployee in the database
        List<CompanyEmployee> companyEmployeeList = companyEmployeeRepository.findAll();
        assertThat(companyEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompanyEmployee() throws Exception {
        // Initialize the database
        companyEmployeeRepository.saveAndFlush(companyEmployee);

        int databaseSizeBeforeDelete = companyEmployeeRepository.findAll().size();

        // Delete the companyEmployee
        restCompanyEmployeeMockMvc
            .perform(delete(ENTITY_API_URL_ID, companyEmployee.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CompanyEmployee> companyEmployeeList = companyEmployeeRepository.findAll();
        assertThat(companyEmployeeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
