import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tbl-working-hours.test-samples';

import { TblWorkingHoursFormService } from './tbl-working-hours-form.service';

describe('TblWorkingHours Form Service', () => {
  let service: TblWorkingHoursFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TblWorkingHoursFormService);
  });

  describe('Service methods', () => {
    describe('createTblWorkingHoursFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTblWorkingHoursFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            day: expect.any(Object),
            startTimeHour: expect.any(Object),
            startTimeMinute: expect.any(Object),
            endTimeHour: expect.any(Object),
            endTimeMinute: expect.any(Object),
          })
        );
      });

      it('passing ITblWorkingHours should create a new form with FormGroup', () => {
        const formGroup = service.createTblWorkingHoursFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            day: expect.any(Object),
            startTimeHour: expect.any(Object),
            startTimeMinute: expect.any(Object),
            endTimeHour: expect.any(Object),
            endTimeMinute: expect.any(Object),
          })
        );
      });
    });

    describe('getTblWorkingHours', () => {
      it('should return NewTblWorkingHours for default TblWorkingHours initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTblWorkingHoursFormGroup(sampleWithNewData);

        const tblWorkingHours = service.getTblWorkingHours(formGroup) as any;

        expect(tblWorkingHours).toMatchObject(sampleWithNewData);
      });

      it('should return NewTblWorkingHours for empty TblWorkingHours initial value', () => {
        const formGroup = service.createTblWorkingHoursFormGroup();

        const tblWorkingHours = service.getTblWorkingHours(formGroup) as any;

        expect(tblWorkingHours).toMatchObject({});
      });

      it('should return ITblWorkingHours', () => {
        const formGroup = service.createTblWorkingHoursFormGroup(sampleWithRequiredData);

        const tblWorkingHours = service.getTblWorkingHours(formGroup) as any;

        expect(tblWorkingHours).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITblWorkingHours should not enable id FormControl', () => {
        const formGroup = service.createTblWorkingHoursFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTblWorkingHours should disable id FormControl', () => {
        const formGroup = service.createTblWorkingHoursFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
