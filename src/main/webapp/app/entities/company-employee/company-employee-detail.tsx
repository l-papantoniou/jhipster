import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './company-employee.reducer';

export const CompanyEmployeeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const companyEmployeeEntity = useAppSelector(state => state.companyEmployee.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="companyEmployeeDetailsHeading">Company Employee</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{companyEmployeeEntity.id}</dd>
          <dt>Company</dt>
          <dd>{companyEmployeeEntity.company ? companyEmployeeEntity.company.id : ''}</dd>
          <dt>Employee</dt>
          <dd>{companyEmployeeEntity.employee ? companyEmployeeEntity.employee.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/company-employee" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/company-employee/${companyEmployeeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CompanyEmployeeDetail;
