import employee from 'app/entities/employee/employee.reducer';
import company from 'app/entities/company/company.reducer';
import companyEmployee from 'app/entities/company-employee/company-employee.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  employee,
  company,
  companyEmployee,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
