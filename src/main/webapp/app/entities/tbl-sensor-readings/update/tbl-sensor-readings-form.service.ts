import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITblSensorReadings, NewTblSensorReadings } from '../tbl-sensor-readings.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITblSensorReadings for edit and NewTblSensorReadingsFormGroupInput for create.
 */
type TblSensorReadingsFormGroupInput = ITblSensorReadings | PartialWithRequiredKeyOf<NewTblSensorReadings>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITblSensorReadings | NewTblSensorReadings> = Omit<T, 'readingTime'> & {
  readingTime?: string | null;
};

type TblSensorReadingsFormRawValue = FormValueOf<ITblSensorReadings>;

type NewTblSensorReadingsFormRawValue = FormValueOf<NewTblSensorReadings>;

type TblSensorReadingsFormDefaults = Pick<NewTblSensorReadings, 'id' | 'readingTime'>;

type TblSensorReadingsFormGroupContent = {
  id: FormControl<TblSensorReadingsFormRawValue['id'] | NewTblSensorReadings['id']>;
  employeeId: FormControl<TblSensorReadingsFormRawValue['employeeId']>;
  readingTime: FormControl<TblSensorReadingsFormRawValue['readingTime']>;
};

export type TblSensorReadingsFormGroup = FormGroup<TblSensorReadingsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TblSensorReadingsFormService {
  createTblSensorReadingsFormGroup(tblSensorReadings: TblSensorReadingsFormGroupInput = { id: null }): TblSensorReadingsFormGroup {
    const tblSensorReadingsRawValue = this.convertTblSensorReadingsToTblSensorReadingsRawValue({
      ...this.getFormDefaults(),
      ...tblSensorReadings,
    });
    return new FormGroup<TblSensorReadingsFormGroupContent>({
      id: new FormControl(
        { value: tblSensorReadingsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      employeeId: new FormControl(tblSensorReadingsRawValue.employeeId),
      readingTime: new FormControl(tblSensorReadingsRawValue.readingTime),
    });
  }

  getTblSensorReadings(form: TblSensorReadingsFormGroup): ITblSensorReadings | NewTblSensorReadings {
    return this.convertTblSensorReadingsRawValueToTblSensorReadings(
      form.getRawValue() as TblSensorReadingsFormRawValue | NewTblSensorReadingsFormRawValue
    );
  }

  resetForm(form: TblSensorReadingsFormGroup, tblSensorReadings: TblSensorReadingsFormGroupInput): void {
    const tblSensorReadingsRawValue = this.convertTblSensorReadingsToTblSensorReadingsRawValue({
      ...this.getFormDefaults(),
      ...tblSensorReadings,
    });
    form.reset(
      {
        ...tblSensorReadingsRawValue,
        id: { value: tblSensorReadingsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TblSensorReadingsFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      readingTime: currentTime,
    };
  }

  private convertTblSensorReadingsRawValueToTblSensorReadings(
    rawTblSensorReadings: TblSensorReadingsFormRawValue | NewTblSensorReadingsFormRawValue
  ): ITblSensorReadings | NewTblSensorReadings {
    return {
      ...rawTblSensorReadings,
      readingTime: dayjs(rawTblSensorReadings.readingTime, DATE_TIME_FORMAT),
    };
  }

  private convertTblSensorReadingsToTblSensorReadingsRawValue(
    tblSensorReadings: ITblSensorReadings | (Partial<NewTblSensorReadings> & TblSensorReadingsFormDefaults)
  ): TblSensorReadingsFormRawValue | PartialWithRequiredKeyOf<NewTblSensorReadingsFormRawValue> {
    return {
      ...tblSensorReadings,
      readingTime: tblSensorReadings.readingTime ? tblSensorReadings.readingTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
