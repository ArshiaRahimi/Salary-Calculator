import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tbl-employee-salary.test-samples';

import { TblEmployeeSalaryFormService } from './tbl-employee-salary-form.service';

describe('TblEmployeeSalary Form Service', () => {
  let service: TblEmployeeSalaryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TblEmployeeSalaryFormService);
  });

  describe('Service methods', () => {
    describe('createTblEmployeeSalaryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTblEmployeeSalaryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateCalculated: expect.any(Object),
            employeeId: expect.any(Object),
            undertime: expect.any(Object),
            overtime: expect.any(Object),
            overtimeOffday: expect.any(Object),
            totalSalary: expect.any(Object),
          })
        );
      });

      it('passing ITblEmployeeSalary should create a new form with FormGroup', () => {
        const formGroup = service.createTblEmployeeSalaryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateCalculated: expect.any(Object),
            employeeId: expect.any(Object),
            undertime: expect.any(Object),
            overtime: expect.any(Object),
            overtimeOffday: expect.any(Object),
            totalSalary: expect.any(Object),
          })
        );
      });
    });

    describe('getTblEmployeeSalary', () => {
      it('should return NewTblEmployeeSalary for default TblEmployeeSalary initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTblEmployeeSalaryFormGroup(sampleWithNewData);

        const tblEmployeeSalary = service.getTblEmployeeSalary(formGroup) as any;

        expect(tblEmployeeSalary).toMatchObject(sampleWithNewData);
      });

      it('should return NewTblEmployeeSalary for empty TblEmployeeSalary initial value', () => {
        const formGroup = service.createTblEmployeeSalaryFormGroup();

        const tblEmployeeSalary = service.getTblEmployeeSalary(formGroup) as any;

        expect(tblEmployeeSalary).toMatchObject({});
      });

      it('should return ITblEmployeeSalary', () => {
        const formGroup = service.createTblEmployeeSalaryFormGroup(sampleWithRequiredData);

        const tblEmployeeSalary = service.getTblEmployeeSalary(formGroup) as any;

        expect(tblEmployeeSalary).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITblEmployeeSalary should not enable id FormControl', () => {
        const formGroup = service.createTblEmployeeSalaryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTblEmployeeSalary should disable id FormControl', () => {
        const formGroup = service.createTblEmployeeSalaryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
