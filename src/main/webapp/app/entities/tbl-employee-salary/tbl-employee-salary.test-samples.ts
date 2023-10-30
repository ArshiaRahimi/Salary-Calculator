import dayjs from 'dayjs/esm';

import { ITblEmployeeSalary, NewTblEmployeeSalary } from './tbl-employee-salary.model';

export const sampleWithRequiredData: ITblEmployeeSalary = {
  id: 52311,
};

export const sampleWithPartialData: ITblEmployeeSalary = {
  id: 61390,
};

export const sampleWithFullData: ITblEmployeeSalary = {
  id: 24861,
  dateCalculated: dayjs('2023-10-30T13:03'),
  employeeId: 66104,
  undertime: 'maximized',
  overtime: 'Missouri synergies International',
  overtimeOffday: 'Car XML',
  totalSalary: 'Profound Manager Towels',
};

export const sampleWithNewData: NewTblEmployeeSalary = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
