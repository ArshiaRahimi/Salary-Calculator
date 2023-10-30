export interface ITblWorkingHours {
  id: number;
  day?: string | null;
  startTimeHour?: number | null;
  startTimeMinute?: number | null;
  endTimeHour?: number | null;
  endTimeMinute?: number | null;
}

export type NewTblWorkingHours = Omit<ITblWorkingHours, 'id'> & { id: null };
