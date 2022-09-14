import dayjs from 'dayjs';

export interface ICompany {
  id?: number;
  name?: string | null;
  afm?: string;
  establishmentYear?: string | null;
  phone?: string | null;
}

export const defaultValue: Readonly<ICompany> = {};
