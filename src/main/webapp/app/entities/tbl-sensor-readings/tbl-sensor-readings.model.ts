import dayjs from 'dayjs/esm';

export interface ITblSensorReadings {
  id: number;
  employeeId?: number | null;
  readingTime?: dayjs.Dayjs | null;
}

export type NewTblSensorReadings = Omit<ITblSensorReadings, 'id'> & { id: null };
