import dayjs from 'dayjs';

export interface IEmployee {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  birthDate?: string | null;
  afm?: string;
}

export const defaultValue: Readonly<IEmployee> = {};
