import { ITblOffDay, NewTblOffDay } from './tbl-off-day.model';

export const sampleWithRequiredData: ITblOffDay = {
  id: 63609,
};

export const sampleWithPartialData: ITblOffDay = {
  id: 56427,
  day: 24576,
  month: 18135,
};

export const sampleWithFullData: ITblOffDay = {
  id: 97056,
  day: 79895,
  month: 24611,
  year: 91578,
};

export const sampleWithNewData: NewTblOffDay = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
