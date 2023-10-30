import { ITblEmployeeInformation, NewTblEmployeeInformation } from './tbl-employee-information.model';

export const sampleWithRequiredData: ITblEmployeeInformation = {
  id: 80505,
};

export const sampleWithPartialData: ITblEmployeeInformation = {
  id: 82250,
  familyName: 'Gloves CSS generate',
  phoneNumber: 'Tugrik',
};

export const sampleWithFullData: ITblEmployeeInformation = {
  id: 83828,
  name: 'Interactions transmitting',
  familyName: 'bypass Cambridgeshire Direct',
  phoneNumber: 'Towels Turkey',
};

export const sampleWithNewData: NewTblEmployeeInformation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
