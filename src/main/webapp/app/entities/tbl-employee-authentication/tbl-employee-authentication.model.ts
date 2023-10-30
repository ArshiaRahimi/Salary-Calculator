export interface ITblEmployeeAuthentication {
  id: number;
  employeeId?: number | null;
  rfidId?: string | null;
  fingerprint?: string | null;
  isActive?: number | null;
}

export type NewTblEmployeeAuthentication = Omit<ITblEmployeeAuthentication, 'id'> & { id: null };
