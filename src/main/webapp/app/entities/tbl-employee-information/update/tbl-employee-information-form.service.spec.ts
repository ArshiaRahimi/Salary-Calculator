import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tbl-employee-information.test-samples';

import { TblEmployeeInformationFormService } from './tbl-employee-information-form.service';

describe('TblEmployeeInformation Form Service', () => {
  let service: TblEmployeeInformationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TblEmployeeInformationFormService);
  });

  describe('Service methods', () => {
    describe('createTblEmployeeInformationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTblEmployeeInformationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            familyName: expect.any(Object),
            phoneNumber: expect.any(Object),
          })
        );
      });

      it('passing ITblEmployeeInformation should create a new form with FormGroup', () => {
        const formGroup = service.createTblEmployeeInformationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            familyName: expect.any(Object),
            phoneNumber: expect.any(Object),
          })
        );
      });
    });

    describe('getTblEmployeeInformation', () => {
      it('should return NewTblEmployeeInformation for default TblEmployeeInformation initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTblEmployeeInformationFormGroup(sampleWithNewData);

        const tblEmployeeInformation = service.getTblEmployeeInformation(formGroup) as any;

        expect(tblEmployeeInformation).toMatchObject(sampleWithNewData);
      });

      it('should return NewTblEmployeeInformation for empty TblEmployeeInformation initial value', () => {
        const formGroup = service.createTblEmployeeInformationFormGroup();

        const tblEmployeeInformation = service.getTblEmployeeInformation(formGroup) as any;

        expect(tblEmployeeInformation).toMatchObject({});
      });

      it('should return ITblEmployeeInformation', () => {
        const formGroup = service.createTblEmployeeInformationFormGroup(sampleWithRequiredData);

        const tblEmployeeInformation = service.getTblEmployeeInformation(formGroup) as any;

        expect(tblEmployeeInformation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITblEmployeeInformation should not enable id FormControl', () => {
        const formGroup = service.createTblEmployeeInformationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTblEmployeeInformation should disable id FormControl', () => {
        const formGroup = service.createTblEmployeeInformationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
