import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITblWorkingHours, NewTblWorkingHours } from '../tbl-working-hours.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITblWorkingHours for edit and NewTblWorkingHoursFormGroupInput for create.
 */
type TblWorkingHoursFormGroupInput = ITblWorkingHours | PartialWithRequiredKeyOf<NewTblWorkingHours>;

type TblWorkingHoursFormDefaults = Pick<NewTblWorkingHours, 'id'>;

type TblWorkingHoursFormGroupContent = {
  id: FormControl<ITblWorkingHours['id'] | NewTblWorkingHours['id']>;
  day: FormControl<ITblWorkingHours['day']>;
  startTimeHour: FormControl<ITblWorkingHours['startTimeHour']>;
  startTimeMinute: FormControl<ITblWorkingHours['startTimeMinute']>;
  endTimeHour: FormControl<ITblWorkingHours['endTimeHour']>;
  endTimeMinute: FormControl<ITblWorkingHours['endTimeMinute']>;
};

export type TblWorkingHoursFormGroup = FormGroup<TblWorkingHoursFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TblWorkingHoursFormService {
  createTblWorkingHoursFormGroup(tblWorkingHours: TblWorkingHoursFormGroupInput = { id: null }): TblWorkingHoursFormGroup {
    const tblWorkingHoursRawValue = {
      ...this.getFormDefaults(),
      ...tblWorkingHours,
    };
    return new FormGroup<TblWorkingHoursFormGroupContent>({
      id: new FormControl(
        { value: tblWorkingHoursRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      day: new FormControl(tblWorkingHoursRawValue.day),
      startTimeHour: new FormControl(tblWorkingHoursRawValue.startTimeHour),
      startTimeMinute: new FormControl(tblWorkingHoursRawValue.startTimeMinute),
      endTimeHour: new FormControl(tblWorkingHoursRawValue.endTimeHour),
      endTimeMinute: new FormControl(tblWorkingHoursRawValue.endTimeMinute),
    });
  }

  getTblWorkingHours(form: TblWorkingHoursFormGroup): ITblWorkingHours | NewTblWorkingHours {
    return form.getRawValue() as ITblWorkingHours | NewTblWorkingHours;
  }

  resetForm(form: TblWorkingHoursFormGroup, tblWorkingHours: TblWorkingHoursFormGroupInput): void {
    const tblWorkingHoursRawValue = { ...this.getFormDefaults(), ...tblWorkingHours };
    form.reset(
      {
        ...tblWorkingHoursRawValue,
        id: { value: tblWorkingHoursRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TblWorkingHoursFormDefaults {
    return {
      id: null,
    };
  }
}
