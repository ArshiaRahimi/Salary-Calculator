import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tbl-employee-authentication.test-samples';

import { TblEmployeeAuthenticationFormService } from './tbl-employee-authentication-form.service';

describe('TblEmployeeAuthentication Form Service', () => {
  let service: TblEmployeeAuthenticationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TblEmployeeAuthenticationFormService);
  });

  describe('Service methods', () => {
    describe('createTblEmployeeAuthenticationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTblEmployeeAuthenticationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            employeeId: expect.any(Object),
            rfidId: expect.any(Object),
            fingerprint: expect.any(Object),
            isActive: expect.any(Object),
          })
        );
      });

      it('passing ITblEmployeeAuthentication should create a new form with FormGroup', () => {
        const formGroup = service.createTblEmployeeAuthenticationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            employeeId: expect.any(Object),
            rfidId: expect.any(Object),
            fingerprint: expect.any(Object),
            isActive: expect.any(Object),
          })
        );
      });
    });

    describe('getTblEmployeeAuthentication', () => {
      it('should return NewTblEmployeeAuthentication for default TblEmployeeAuthentication initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTblEmployeeAuthenticationFormGroup(sampleWithNewData);

        const tblEmployeeAuthentication = service.getTblEmployeeAuthentication(formGroup) as any;

        expect(tblEmployeeAuthentication).toMatchObject(sampleWithNewData);
      });

      it('should return NewTblEmployeeAuthentication for empty TblEmployeeAuthentication initial value', () => {
        const formGroup = service.createTblEmployeeAuthenticationFormGroup();

        const tblEmployeeAuthentication = service.getTblEmployeeAuthentication(formGroup) as any;

        expect(tblEmployeeAuthentication).toMatchObject({});
      });

      it('should return ITblEmployeeAuthentication', () => {
        const formGroup = service.createTblEmployeeAuthenticationFormGroup(sampleWithRequiredData);

        const tblEmployeeAuthentication = service.getTblEmployeeAuthentication(formGroup) as any;

        expect(tblEmployeeAuthentication).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITblEmployeeAuthentication should not enable id FormControl', () => {
        const formGroup = service.createTblEmployeeAuthenticationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTblEmployeeAuthentication should disable id FormControl', () => {
        const formGroup = service.createTblEmployeeAuthenticationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
