import dayjs from 'dayjs/esm';

export interface ITblEmployeeSalary {
  id: number;
  dateCalculated?: dayjs.Dayjs | null;
  employeeId?: number | null;
  undertime?: string | null;
  overtime?: string | null;
  overtimeOffday?: string | null;
  totalSalary?: string | null;
}

export type NewTblEmployeeSalary = Omit<ITblEmployeeSalary, 'id'> & { id: null };
