import dayjs from 'dayjs/esm';

import { ITblSensorReadings, NewTblSensorReadings } from './tbl-sensor-readings.model';

export const sampleWithRequiredData: ITblSensorReadings = {
  id: 54165,
};

export const sampleWithPartialData: ITblSensorReadings = {
  id: 82483,
  readingTime: dayjs('2023-10-28T01:32'),
};

export const sampleWithFullData: ITblSensorReadings = {
  id: 24945,
  employeeId: 21488,
  readingTime: dayjs('2023-10-27T22:01'),
};

export const sampleWithNewData: NewTblSensorReadings = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
