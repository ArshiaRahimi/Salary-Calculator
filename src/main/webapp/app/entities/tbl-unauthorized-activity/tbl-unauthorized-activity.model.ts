import dayjs from 'dayjs/esm';

export interface ITblUnauthorizedActivity {
  id: number;
  rfidId?: string | null;
  employeeId?: number | null;
  readingTime?: dayjs.Dayjs | null;
  attempt?: number | null;
  fingerprint?: string | null;
}

export type NewTblUnauthorizedActivity = Omit<ITblUnauthorizedActivity, 'id'> & { id: null };
