import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITblUnauthorizedActivity, NewTblUnauthorizedActivity } from '../tbl-unauthorized-activity.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITblUnauthorizedActivity for edit and NewTblUnauthorizedActivityFormGroupInput for create.
 */
type TblUnauthorizedActivityFormGroupInput = ITblUnauthorizedActivity | PartialWithRequiredKeyOf<NewTblUnauthorizedActivity>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITblUnauthorizedActivity | NewTblUnauthorizedActivity> = Omit<T, 'readingTime'> & {
  readingTime?: string | null;
};

type TblUnauthorizedActivityFormRawValue = FormValueOf<ITblUnauthorizedActivity>;

type NewTblUnauthorizedActivityFormRawValue = FormValueOf<NewTblUnauthorizedActivity>;

type TblUnauthorizedActivityFormDefaults = Pick<NewTblUnauthorizedActivity, 'id' | 'readingTime'>;

type TblUnauthorizedActivityFormGroupContent = {
  id: FormControl<TblUnauthorizedActivityFormRawValue['id'] | NewTblUnauthorizedActivity['id']>;
  rfidId: FormControl<TblUnauthorizedActivityFormRawValue['rfidId']>;
  employeeId: FormControl<TblUnauthorizedActivityFormRawValue['employeeId']>;
  readingTime: FormControl<TblUnauthorizedActivityFormRawValue['readingTime']>;
  attempt: FormControl<TblUnauthorizedActivityFormRawValue['attempt']>;
  fingerprint: FormControl<TblUnauthorizedActivityFormRawValue['fingerprint']>;
};

export type TblUnauthorizedActivityFormGroup = FormGroup<TblUnauthorizedActivityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TblUnauthorizedActivityFormService {
  createTblUnauthorizedActivityFormGroup(
    tblUnauthorizedActivity: TblUnauthorizedActivityFormGroupInput = { id: null }
  ): TblUnauthorizedActivityFormGroup {
    const tblUnauthorizedActivityRawValue = this.convertTblUnauthorizedActivityToTblUnauthorizedActivityRawValue({
      ...this.getFormDefaults(),
      ...tblUnauthorizedActivity,
    });
    return new FormGroup<TblUnauthorizedActivityFormGroupContent>({
      id: new FormControl(
        { value: tblUnauthorizedActivityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      rfidId: new FormControl(tblUnauthorizedActivityRawValue.rfidId),
      employeeId: new FormControl(tblUnauthorizedActivityRawValue.employeeId),
      readingTime: new FormControl(tblUnauthorizedActivityRawValue.readingTime),
      attempt: new FormControl(tblUnauthorizedActivityRawValue.attempt),
      fingerprint: new FormControl(tblUnauthorizedActivityRawValue.fingerprint),
    });
  }

  getTblUnauthorizedActivity(form: TblUnauthorizedActivityFormGroup): ITblUnauthorizedActivity | NewTblUnauthorizedActivity {
    return this.convertTblUnauthorizedActivityRawValueToTblUnauthorizedActivity(
      form.getRawValue() as TblUnauthorizedActivityFormRawValue | NewTblUnauthorizedActivityFormRawValue
    );
  }

  resetForm(form: TblUnauthorizedActivityFormGroup, tblUnauthorizedActivity: TblUnauthorizedActivityFormGroupInput): void {
    const tblUnauthorizedActivityRawValue = this.convertTblUnauthorizedActivityToTblUnauthorizedActivityRawValue({
      ...this.getFormDefaults(),
      ...tblUnauthorizedActivity,
    });
    form.reset(
      {
        ...tblUnauthorizedActivityRawValue,
        id: { value: tblUnauthorizedActivityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TblUnauthorizedActivityFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      readingTime: currentTime,
    };
  }

  private convertTblUnauthorizedActivityRawValueToTblUnauthorizedActivity(
    rawTblUnauthorizedActivity: TblUnauthorizedActivityFormRawValue | NewTblUnauthorizedActivityFormRawValue
  ): ITblUnauthorizedActivity | NewTblUnauthorizedActivity {
    return {
      ...rawTblUnauthorizedActivity,
      readingTime: dayjs(rawTblUnauthorizedActivity.readingTime, DATE_TIME_FORMAT),
    };
  }

  private convertTblUnauthorizedActivityToTblUnauthorizedActivityRawValue(
    tblUnauthorizedActivity: ITblUnauthorizedActivity | (Partial<NewTblUnauthorizedActivity> & TblUnauthorizedActivityFormDefaults)
  ): TblUnauthorizedActivityFormRawValue | PartialWithRequiredKeyOf<NewTblUnauthorizedActivityFormRawValue> {
    return {
      ...tblUnauthorizedActivity,
      readingTime: tblUnauthorizedActivity.readingTime ? tblUnauthorizedActivity.readingTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
