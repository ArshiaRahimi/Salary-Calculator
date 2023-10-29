import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tbl-sensor-readings.test-samples';

import { TblSensorReadingsFormService } from './tbl-sensor-readings-form.service';

describe('TblSensorReadings Form Service', () => {
  let service: TblSensorReadingsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TblSensorReadingsFormService);
  });

  describe('Service methods', () => {
    describe('createTblSensorReadingsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTblSensorReadingsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            employeeId: expect.any(Object),
            readingTime: expect.any(Object),
          })
        );
      });

      it('passing ITblSensorReadings should create a new form with FormGroup', () => {
        const formGroup = service.createTblSensorReadingsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            employeeId: expect.any(Object),
            readingTime: expect.any(Object),
          })
        );
      });
    });

    describe('getTblSensorReadings', () => {
      it('should return NewTblSensorReadings for default TblSensorReadings initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTblSensorReadingsFormGroup(sampleWithNewData);

        const tblSensorReadings = service.getTblSensorReadings(formGroup) as any;

        expect(tblSensorReadings).toMatchObject(sampleWithNewData);
      });

      it('should return NewTblSensorReadings for empty TblSensorReadings initial value', () => {
        const formGroup = service.createTblSensorReadingsFormGroup();

        const tblSensorReadings = service.getTblSensorReadings(formGroup) as any;

        expect(tblSensorReadings).toMatchObject({});
      });

      it('should return ITblSensorReadings', () => {
        const formGroup = service.createTblSensorReadingsFormGroup(sampleWithRequiredData);

        const tblSensorReadings = service.getTblSensorReadings(formGroup) as any;

        expect(tblSensorReadings).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITblSensorReadings should not enable id FormControl', () => {
        const formGroup = service.createTblSensorReadingsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTblSensorReadings should disable id FormControl', () => {
        const formGroup = service.createTblSensorReadingsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
