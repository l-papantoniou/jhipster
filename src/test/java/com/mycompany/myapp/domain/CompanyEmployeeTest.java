package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompanyEmployeeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyEmployee.class);
        CompanyEmployee companyEmployee1 = new CompanyEmployee();
        companyEmployee1.setId(1L);
        CompanyEmployee companyEmployee2 = new CompanyEmployee();
        companyEmployee2.setId(companyEmployee1.getId());
        assertThat(companyEmployee1).isEqualTo(companyEmployee2);
        companyEmployee2.setId(2L);
        assertThat(companyEmployee1).isNotEqualTo(companyEmployee2);
        companyEmployee1.setId(null);
        assertThat(companyEmployee1).isNotEqualTo(companyEmployee2);
    }
}
