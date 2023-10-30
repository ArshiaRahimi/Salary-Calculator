export interface ITblSalaryInfo {
  id: number;
  employeeId?: number | null;
  baseSalary?: number | null;
  housingRights?: number | null;
  internetRights?: number | null;
  groceriesRights?: number | null;
}

export type NewTblSalaryInfo = Omit<ITblSalaryInfo, 'id'> & { id: null };
