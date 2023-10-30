import { ITblSalaryInfo, NewTblSalaryInfo } from './tbl-salary-info.model';

export const sampleWithRequiredData: ITblSalaryInfo = {
  id: 52383,
};

export const sampleWithPartialData: ITblSalaryInfo = {
  id: 38296,
  baseSalary: 67197,
  housingRights: 31586,
  groceriesRights: 59460,
};

export const sampleWithFullData: ITblSalaryInfo = {
  id: 43417,
  employeeId: 28825,
  baseSalary: 52202,
  housingRights: 10979,
  internetRights: 78679,
  groceriesRights: 79890,
};

export const sampleWithNewData: NewTblSalaryInfo = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
