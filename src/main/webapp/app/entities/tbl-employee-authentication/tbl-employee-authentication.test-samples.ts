import { ITblEmployeeAuthentication, NewTblEmployeeAuthentication } from './tbl-employee-authentication.model';

export const sampleWithRequiredData: ITblEmployeeAuthentication = {
  id: 18842,
};

export const sampleWithPartialData: ITblEmployeeAuthentication = {
  id: 85240,
  employeeId: 66681,
  rfidId: 'robust',
  fingerprint: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: ITblEmployeeAuthentication = {
  id: 39430,
  employeeId: 2051,
  rfidId: 'deploy bluetooth productize',
  fingerprint: '../fake-data/blob/hipster.txt',
  isActive: 78333,
};

export const sampleWithNewData: NewTblEmployeeAuthentication = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
