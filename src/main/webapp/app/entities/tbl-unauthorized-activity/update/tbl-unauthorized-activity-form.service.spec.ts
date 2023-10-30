import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tbl-unauthorized-activity.test-samples';

import { TblUnauthorizedActivityFormService } from './tbl-unauthorized-activity-form.service';

describe('TblUnauthorizedActivity Form Service', () => {
  let service: TblUnauthorizedActivityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TblUnauthorizedActivityFormService);
  });

  describe('Service methods', () => {
    describe('createTblUnauthorizedActivityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTblUnauthorizedActivityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            rfidId: expect.any(Object),
            employeeId: expect.any(Object),
            readingTime: expect.any(Object),
            attempt: expect.any(Object),
            fingerprint: expect.any(Object),
          })
        );
      });

      it('passing ITblUnauthorizedActivity should create a new form with FormGroup', () => {
        const formGroup = service.createTblUnauthorizedActivityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            rfidId: expect.any(Object),
            employeeId: expect.any(Object),
            readingTime: expect.any(Object),
            attempt: expect.any(Object),
            fingerprint: expect.any(Object),
          })
        );
      });
    });

    describe('getTblUnauthorizedActivity', () => {
      it('should return NewTblUnauthorizedActivity for default TblUnauthorizedActivity initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTblUnauthorizedActivityFormGroup(sampleWithNewData);

        const tblUnauthorizedActivity = service.getTblUnauthorizedActivity(formGroup) as any;

        expect(tblUnauthorizedActivity).toMatchObject(sampleWithNewData);
      });

      it('should return NewTblUnauthorizedActivity for empty TblUnauthorizedActivity initial value', () => {
        const formGroup = service.createTblUnauthorizedActivityFormGroup();

        const tblUnauthorizedActivity = service.getTblUnauthorizedActivity(formGroup) as any;

        expect(tblUnauthorizedActivity).toMatchObject({});
      });

      it('should return ITblUnauthorizedActivity', () => {
        const formGroup = service.createTblUnauthorizedActivityFormGroup(sampleWithRequiredData);

        const tblUnauthorizedActivity = service.getTblUnauthorizedActivity(formGroup) as any;

        expect(tblUnauthorizedActivity).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITblUnauthorizedActivity should not enable id FormControl', () => {
        const formGroup = service.createTblUnauthorizedActivityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTblUnauthorizedActivity should disable id FormControl', () => {
        const formGroup = service.createTblUnauthorizedActivityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
