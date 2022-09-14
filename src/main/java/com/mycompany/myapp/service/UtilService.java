package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.CompanyEmployee;
import com.mycompany.myapp.domain.Employee;
import com.mycompany.myapp.repository.CompanyEmployeeRepository;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

/**
 * Service Interface for managing {@link CompanyEmployee}.
 */
@Service
@Transactional
public class UtilService {

    private CompanyEmployeeRepository companyEmployeeRepository;

    public List<CompanyEmployee> getEmployeesByCompany(Long id) {
        return companyEmployeeRepository.getEmployeesByCompany(id);
    }
}
