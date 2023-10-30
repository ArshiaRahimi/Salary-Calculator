import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITblOffDay, NewTblOffDay } from '../tbl-off-day.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITblOffDay for edit and NewTblOffDayFormGroupInput for create.
 */
type TblOffDayFormGroupInput = ITblOffDay | PartialWithRequiredKeyOf<NewTblOffDay>;

type TblOffDayFormDefaults = Pick<NewTblOffDay, 'id'>;

type TblOffDayFormGroupContent = {
  id: FormControl<ITblOffDay['id'] | NewTblOffDay['id']>;
  day: FormControl<ITblOffDay['day']>;
  month: FormControl<ITblOffDay['month']>;
  year: FormControl<ITblOffDay['year']>;
};

export type TblOffDayFormGroup = FormGroup<TblOffDayFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TblOffDayFormService {
  createTblOffDayFormGroup(tblOffDay: TblOffDayFormGroupInput = { id: null }): TblOffDayFormGroup {
    const tblOffDayRawValue = {
      ...this.getFormDefaults(),
      ...tblOffDay,
    };
    return new FormGroup<TblOffDayFormGroupContent>({
      id: new FormControl(
        { value: tblOffDayRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      day: new FormControl(tblOffDayRawValue.day),
      month: new FormControl(tblOffDayRawValue.month),
      year: new FormControl(tblOffDayRawValue.year),
    });
  }

  getTblOffDay(form: TblOffDayFormGroup): ITblOffDay | NewTblOffDay {
    return form.getRawValue() as ITblOffDay | NewTblOffDay;
  }

  resetForm(form: TblOffDayFormGroup, tblOffDay: TblOffDayFormGroupInput): void {
    const tblOffDayRawValue = { ...this.getFormDefaults(), ...tblOffDay };
    form.reset(
      {
        ...tblOffDayRawValue,
        id: { value: tblOffDayRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TblOffDayFormDefaults {
    return {
      id: null,
    };
  }
}
