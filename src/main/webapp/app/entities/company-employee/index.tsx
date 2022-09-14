import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CompanyEmployee from './company-employee';
import CompanyEmployeeDetail from './company-employee-detail';
import CompanyEmployeeUpdate from './company-employee-update';
import CompanyEmployeeDeleteDialog from './company-employee-delete-dialog';

const CompanyEmployeeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CompanyEmployee />} />
    <Route path="new" element={<CompanyEmployeeUpdate />} />
    <Route path=":id">
      <Route index element={<CompanyEmployeeDetail />} />
      <Route path="edit" element={<CompanyEmployeeUpdate />} />
      <Route path="delete" element={<CompanyEmployeeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CompanyEmployeeRoutes;
