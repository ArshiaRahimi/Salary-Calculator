export interface ITblOffDay {
  id: number;
  day?: number | null;
  month?: number | null;
  year?: number | null;
}

export type NewTblOffDay = Omit<ITblOffDay, 'id'> & { id: null };
