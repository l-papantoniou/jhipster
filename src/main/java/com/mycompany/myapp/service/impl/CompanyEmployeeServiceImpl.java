package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.CompanyEmployee;
import com.mycompany.myapp.repository.CompanyEmployeeRepository;
import com.mycompany.myapp.service.CompanyEmployeeService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CompanyEmployee}.
 */
@Service
@Transactional
public class CompanyEmployeeServiceImpl implements CompanyEmployeeService {

    private final Logger log = LoggerFactory.getLogger(CompanyEmployeeServiceImpl.class);

    private final CompanyEmployeeRepository companyEmployeeRepository;

    public CompanyEmployeeServiceImpl(CompanyEmployeeRepository companyEmployeeRepository) {
        this.companyEmployeeRepository = companyEmployeeRepository;
    }

    @Override
    public CompanyEmployee save(CompanyEmployee companyEmployee) {
        log.debug("Request to save CompanyEmployee : {}", companyEmployee);
        return companyEmployeeRepository.save(companyEmployee);
    }

    @Override
    public CompanyEmployee update(CompanyEmployee companyEmployee) {
        log.debug("Request to save CompanyEmployee : {}", companyEmployee);
        return companyEmployeeRepository.save(companyEmployee);
    }

    @Override
    public Optional<CompanyEmployee> partialUpdate(CompanyEmployee companyEmployee) {
        log.debug("Request to partially update CompanyEmployee : {}", companyEmployee);

        return companyEmployeeRepository
            .findById(companyEmployee.getId())
            .map(existingCompanyEmployee -> {
                return existingCompanyEmployee;
            })
            .map(companyEmployeeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyEmployee> findAll() {
        log.debug("Request to get all CompanyEmployees");
        return companyEmployeeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CompanyEmployee> findOne(Long id) {
        log.debug("Request to get CompanyEmployee : {}", id);
        return companyEmployeeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CompanyEmployee : {}", id);
        companyEmployeeRepository.deleteById(id);
    }
}
