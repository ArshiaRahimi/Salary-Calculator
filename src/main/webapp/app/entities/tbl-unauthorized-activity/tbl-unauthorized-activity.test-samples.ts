import dayjs from 'dayjs/esm';

import { ITblUnauthorizedActivity, NewTblUnauthorizedActivity } from './tbl-unauthorized-activity.model';

export const sampleWithRequiredData: ITblUnauthorizedActivity = {
  id: 95085,
};

export const sampleWithPartialData: ITblUnauthorizedActivity = {
  id: 12820,
  employeeId: 86539,
  fingerprint: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: ITblUnauthorizedActivity = {
  id: 71215,
  rfidId: 'Web Legacy Pizza',
  employeeId: 94226,
  readingTime: dayjs('2023-10-30T00:17'),
  attempt: 54790,
  fingerprint: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewTblUnauthorizedActivity = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
