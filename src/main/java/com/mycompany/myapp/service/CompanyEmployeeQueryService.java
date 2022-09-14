package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.CompanyEmployee;
import com.mycompany.myapp.repository.CompanyEmployeeRepository;
import com.mycompany.myapp.service.criteria.CompanyEmployeeCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CompanyEmployee} entities in the database.
 * The main input is a {@link CompanyEmployeeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CompanyEmployee} or a {@link Page} of {@link CompanyEmployee} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CompanyEmployeeQueryService extends QueryService<CompanyEmployee> {

    private final Logger log = LoggerFactory.getLogger(CompanyEmployeeQueryService.class);

    private final CompanyEmployeeRepository companyEmployeeRepository;

    public CompanyEmployeeQueryService(CompanyEmployeeRepository companyEmployeeRepository) {
        this.companyEmployeeRepository = companyEmployeeRepository;
    }

    /**
     * Return a {@link List} of {@link CompanyEmployee} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CompanyEmployee> findByCriteria(CompanyEmployeeCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<CompanyEmployee> specification = createSpecification(criteria);
        return companyEmployeeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CompanyEmployee} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CompanyEmployee> findByCriteria(CompanyEmployeeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<CompanyEmployee> specification = createSpecification(criteria);
        return companyEmployeeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CompanyEmployeeCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<CompanyEmployee> specification = createSpecification(criteria);
        return companyEmployeeRepository.count(specification);
    }

    /**
     * Function to convert {@link CompanyEmployeeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CompanyEmployee> createSpecification(CompanyEmployeeCriteria criteria) {
        Specification<CompanyEmployee> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CompanyEmployee_.id));
            }
            if (criteria.getCompanyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCompanyId(),
                            root -> root.join(CompanyEmployee_.company, JoinType.LEFT).get(Company_.id)
                        )
                    );
            }
            if (criteria.getEmployeeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmployeeId(),
                            root -> root.join(CompanyEmployee_.employee, JoinType.LEFT).get(Employee_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
