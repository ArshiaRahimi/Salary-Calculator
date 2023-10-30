import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tbl-salary-info.test-samples';

import { TblSalaryInfoFormService } from './tbl-salary-info-form.service';

describe('TblSalaryInfo Form Service', () => {
  let service: TblSalaryInfoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TblSalaryInfoFormService);
  });

  describe('Service methods', () => {
    describe('createTblSalaryInfoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTblSalaryInfoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            employeeId: expect.any(Object),
            baseSalary: expect.any(Object),
            housingRights: expect.any(Object),
            internetRights: expect.any(Object),
            groceriesRights: expect.any(Object),
          })
        );
      });

      it('passing ITblSalaryInfo should create a new form with FormGroup', () => {
        const formGroup = service.createTblSalaryInfoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            employeeId: expect.any(Object),
            baseSalary: expect.any(Object),
            housingRights: expect.any(Object),
            internetRights: expect.any(Object),
            groceriesRights: expect.any(Object),
          })
        );
      });
    });

    describe('getTblSalaryInfo', () => {
      it('should return NewTblSalaryInfo for default TblSalaryInfo initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTblSalaryInfoFormGroup(sampleWithNewData);

        const tblSalaryInfo = service.getTblSalaryInfo(formGroup) as any;

        expect(tblSalaryInfo).toMatchObject(sampleWithNewData);
      });

      it('should return NewTblSalaryInfo for empty TblSalaryInfo initial value', () => {
        const formGroup = service.createTblSalaryInfoFormGroup();

        const tblSalaryInfo = service.getTblSalaryInfo(formGroup) as any;

        expect(tblSalaryInfo).toMatchObject({});
      });

      it('should return ITblSalaryInfo', () => {
        const formGroup = service.createTblSalaryInfoFormGroup(sampleWithRequiredData);

        const tblSalaryInfo = service.getTblSalaryInfo(formGroup) as any;

        expect(tblSalaryInfo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITblSalaryInfo should not enable id FormControl', () => {
        const formGroup = service.createTblSalaryInfoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTblSalaryInfo should disable id FormControl', () => {
        const formGroup = service.createTblSalaryInfoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
