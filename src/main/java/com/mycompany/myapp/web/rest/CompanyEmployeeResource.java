package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.CompanyEmployee;
import com.mycompany.myapp.domain.Employee;
import com.mycompany.myapp.repository.CompanyEmployeeRepository;
import com.mycompany.myapp.service.CompanyEmployeeQueryService;
import com.mycompany.myapp.service.CompanyEmployeeService;
import com.mycompany.myapp.service.UtilService;
import com.mycompany.myapp.service.criteria.CompanyEmployeeCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.CompanyEmployee}.
 */
@RestController
@RequestMapping("/api")
public class CompanyEmployeeResource {

    private final Logger log = LoggerFactory.getLogger(CompanyEmployeeResource.class);

    private static final String ENTITY_NAME = "companyEmployee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompanyEmployeeService companyEmployeeService;

    private final UtilService utilService;

    private final CompanyEmployeeRepository companyEmployeeRepository;

    private final CompanyEmployeeQueryService companyEmployeeQueryService;

    public CompanyEmployeeResource(
        CompanyEmployeeService companyEmployeeService,
        CompanyEmployeeRepository companyEmployeeRepository,
        CompanyEmployeeQueryService companyEmployeeQueryService,
        UtilService utilService
    ) {
        this.companyEmployeeService = companyEmployeeService;
        this.companyEmployeeRepository = companyEmployeeRepository;
        this.companyEmployeeQueryService = companyEmployeeQueryService;
        this.utilService = utilService;
    }

    /**
     * {@code POST  /company-employees} : Create a new companyEmployee.
     *
     * @param companyEmployee the companyEmployee to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new companyEmployee, or with status {@code 400 (Bad Request)} if the companyEmployee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/company-employees")
    public ResponseEntity<CompanyEmployee> createCompanyEmployee(@RequestBody CompanyEmployee companyEmployee) throws URISyntaxException {
        log.debug("REST request to save CompanyEmployee : {}", companyEmployee);
        if (companyEmployee.getId() != null) {
            throw new BadRequestAlertException("A new companyEmployee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompanyEmployee result = companyEmployeeService.save(companyEmployee);
        return ResponseEntity
            .created(new URI("/api/company-employees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /company-employees/:id} : Updates an existing companyEmployee.
     *
     * @param id the id of the companyEmployee to save.
     * @param companyEmployee the companyEmployee to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyEmployee,
     * or with status {@code 400 (Bad Request)} if the companyEmployee is not valid,
     * or with status {@code 500 (Internal Server Error)} if the companyEmployee couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/company-employees/{id}")
    public ResponseEntity<CompanyEmployee> updateCompanyEmployee(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompanyEmployee companyEmployee
    ) throws URISyntaxException {
        log.debug("REST request to update CompanyEmployee : {}, {}", id, companyEmployee);
        if (companyEmployee.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyEmployee.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companyEmployeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompanyEmployee result = companyEmployeeService.update(companyEmployee);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, companyEmployee.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /company-employees/:id} : Partial updates given fields of an existing companyEmployee, field will ignore if it is null
     *
     * @param id the id of the companyEmployee to save.
     * @param companyEmployee the companyEmployee to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyEmployee,
     * or with status {@code 400 (Bad Request)} if the companyEmployee is not valid,
     * or with status {@code 404 (Not Found)} if the companyEmployee is not found,
     * or with status {@code 500 (Internal Server Error)} if the companyEmployee couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/company-employees/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompanyEmployee> partialUpdateCompanyEmployee(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompanyEmployee companyEmployee
    ) throws URISyntaxException {
        log.debug("REST request to partial update CompanyEmployee partially : {}, {}", id, companyEmployee);
        if (companyEmployee.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyEmployee.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companyEmployeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompanyEmployee> result = companyEmployeeService.partialUpdate(companyEmployee);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, companyEmployee.getId().toString())
        );
    }

    /**
     * {@code GET  /company-employees} : get all the companyEmployees.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of companyEmployees in body.
     */
    @GetMapping("/company-employees")
    public ResponseEntity<List<CompanyEmployee>> getAllCompanyEmployees(CompanyEmployeeCriteria criteria) {
        log.debug("REST request to get CompanyEmployees by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        List<CompanyEmployee> entityList = companyEmployeeQueryService.findByCriteria(criteria);
        //log.debug(utilService.getEmployeesByCompany(1051L).toString());
        // List<Employee> employees = utilService.getEmployeesByCompany(1051L);
        //log.debug(employees.toString());
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /company-employees/count} : count all the companyEmployees.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/company-employees/count")
    public ResponseEntity<Long> countCompanyEmployees(CompanyEmployeeCriteria criteria) {
        log.debug("REST request to count CompanyEmployees by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        return ResponseEntity.ok().body(companyEmployeeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /company-employees/:id} : get the "id" companyEmployee.
     *
     * @param id the id of the companyEmployee to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the companyEmployee, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/company-employees/{id}")
    public ResponseEntity<CompanyEmployee> getCompanyEmployee(@PathVariable Long id) {
        log.debug("REST request to get CompanyEmployee : {}", id);
        Optional<CompanyEmployee> companyEmployee = companyEmployeeService.findOne(id);
        log.debug(utilService.getEmployeesByCompany(id).toString());
        return ResponseUtil.wrapOrNotFound(companyEmployee);
    }

    /**
     * {@code DELETE  /company-employees/:id} : delete the "id" companyEmployee.
     *
     * @param id the id of the companyEmployee to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/company-employees/{id}")
    public ResponseEntity<Void> deleteCompanyEmployee(@PathVariable Long id) {
        log.debug("REST request to delete CompanyEmployee : {}", id);
        companyEmployeeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
