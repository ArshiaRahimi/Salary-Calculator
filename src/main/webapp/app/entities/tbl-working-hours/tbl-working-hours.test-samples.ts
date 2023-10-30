import { ITblWorkingHours, NewTblWorkingHours } from './tbl-working-hours.model';

export const sampleWithRequiredData: ITblWorkingHours = {
  id: 12100,
};

export const sampleWithPartialData: ITblWorkingHours = {
  id: 7393,
  startTimeHour: 68754,
  endTimeHour: 80672,
  endTimeMinute: 87900,
};

export const sampleWithFullData: ITblWorkingHours = {
  id: 70937,
  day: 'Checking Streets',
  startTimeHour: 85765,
  startTimeMinute: 44157,
  endTimeHour: 46606,
  endTimeMinute: 84072,
};

export const sampleWithNewData: NewTblWorkingHours = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
