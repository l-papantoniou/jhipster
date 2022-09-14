import { ICompany } from 'app/shared/model/company.model';
import { IEmployee } from 'app/shared/model/employee.model';

export interface ICompanyEmployee {
  id?: number;
  company?: ICompany | null;
  employee?: IEmployee | null;
}

export const defaultValue: Readonly<ICompanyEmployee> = {};
