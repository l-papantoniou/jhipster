import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICompanyEmployee } from 'app/shared/model/company-employee.model';
import { getEntities } from './company-employee.reducer';

export const CompanyEmployee = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const companyEmployeeList = useAppSelector(state => state.companyEmployee.entities);
  const loading = useAppSelector(state => state.companyEmployee.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="company-employee-heading" data-cy="CompanyEmployeeHeading">
        Company Employees
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/company-employee/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Company Employee
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {companyEmployeeList && companyEmployeeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Company</th>
                <th>Employee</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {companyEmployeeList.map((companyEmployee, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/company-employee/${companyEmployee.id}`} color="link" size="sm">
                      {companyEmployee.id}
                    </Button>
                  </td>
                  <td>
                    {companyEmployee.company ? <Link to={`/company/${companyEmployee.company.id}`}>{companyEmployee.company.id}</Link> : ''}
                  </td>
                  <td>
                    {companyEmployee.employee ? (
                      <Link to={`/employee/${companyEmployee.employee.id}`}>{companyEmployee.employee.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/company-employee/${companyEmployee.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/company-employee/${companyEmployee.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/company-employee/${companyEmployee.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Company Employees found</div>
        )}
      </div>
    </div>
  );
};

export default CompanyEmployee;
