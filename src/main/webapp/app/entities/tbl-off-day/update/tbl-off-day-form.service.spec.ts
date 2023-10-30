import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tbl-off-day.test-samples';

import { TblOffDayFormService } from './tbl-off-day-form.service';

describe('TblOffDay Form Service', () => {
  let service: TblOffDayFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TblOffDayFormService);
  });

  describe('Service methods', () => {
    describe('createTblOffDayFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTblOffDayFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            day: expect.any(Object),
            month: expect.any(Object),
            year: expect.any(Object),
          })
        );
      });

      it('passing ITblOffDay should create a new form with FormGroup', () => {
        const formGroup = service.createTblOffDayFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            day: expect.any(Object),
            month: expect.any(Object),
            year: expect.any(Object),
          })
        );
      });
    });

    describe('getTblOffDay', () => {
      it('should return NewTblOffDay for default TblOffDay initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTblOffDayFormGroup(sampleWithNewData);

        const tblOffDay = service.getTblOffDay(formGroup) as any;

        expect(tblOffDay).toMatchObject(sampleWithNewData);
      });

      it('should return NewTblOffDay for empty TblOffDay initial value', () => {
        const formGroup = service.createTblOffDayFormGroup();

        const tblOffDay = service.getTblOffDay(formGroup) as any;

        expect(tblOffDay).toMatchObject({});
      });

      it('should return ITblOffDay', () => {
        const formGroup = service.createTblOffDayFormGroup(sampleWithRequiredData);

        const tblOffDay = service.getTblOffDay(formGroup) as any;

        expect(tblOffDay).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITblOffDay should not enable id FormControl', () => {
        const formGroup = service.createTblOffDayFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTblOffDay should disable id FormControl', () => {
        const formGroup = service.createTblOffDayFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
