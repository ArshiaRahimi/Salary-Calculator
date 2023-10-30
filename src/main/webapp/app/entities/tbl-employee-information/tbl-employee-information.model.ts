export interface ITblEmployeeInformation {
  id: number;
  name?: string | null;
  familyName?: string | null;
  phoneNumber?: string | null;
}

export type NewTblEmployeeInformation = Omit<ITblEmployeeInformation, 'id'> & { id: null };
