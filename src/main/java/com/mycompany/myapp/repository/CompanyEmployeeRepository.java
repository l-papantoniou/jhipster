package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CompanyEmployee;
import com.mycompany.myapp.domain.Employee;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CompanyEmployee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyEmployeeRepository extends JpaRepository<CompanyEmployee, Long>, JpaSpecificationExecutor<CompanyEmployee> {
    @Query(value = "select companyEmployee from CompanyEmployee companyEmployee where companyEmployee.company.id =:companyId")
    List<CompanyEmployee> getEmployeesByCompany(@Param("companyId") Long companyId);
}
