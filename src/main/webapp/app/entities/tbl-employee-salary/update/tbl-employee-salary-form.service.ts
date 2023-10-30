import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITblEmployeeSalary, NewTblEmployeeSalary } from '../tbl-employee-salary.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITblEmployeeSalary for edit and NewTblEmployeeSalaryFormGroupInput for create.
 */
type TblEmployeeSalaryFormGroupInput = ITblEmployeeSalary | PartialWithRequiredKeyOf<NewTblEmployeeSalary>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITblEmployeeSalary | NewTblEmployeeSalary> = Omit<T, 'dateCalculated'> & {
  dateCalculated?: string | null;
};

type TblEmployeeSalaryFormRawValue = FormValueOf<ITblEmployeeSalary>;

type NewTblEmployeeSalaryFormRawValue = FormValueOf<NewTblEmployeeSalary>;

type TblEmployeeSalaryFormDefaults = Pick<NewTblEmployeeSalary, 'id' | 'dateCalculated'>;

type TblEmployeeSalaryFormGroupContent = {
  id: FormControl<TblEmployeeSalaryFormRawValue['id'] | NewTblEmployeeSalary['id']>;
  dateCalculated: FormControl<TblEmployeeSalaryFormRawValue['dateCalculated']>;
  employeeId: FormControl<TblEmployeeSalaryFormRawValue['employeeId']>;
  undertime: FormControl<TblEmployeeSalaryFormRawValue['undertime']>;
  overtime: FormControl<TblEmployeeSalaryFormRawValue['overtime']>;
  overtimeOffday: FormControl<TblEmployeeSalaryFormRawValue['overtimeOffday']>;
  totalSalary: FormControl<TblEmployeeSalaryFormRawValue['totalSalary']>;
};

export type TblEmployeeSalaryFormGroup = FormGroup<TblEmployeeSalaryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TblEmployeeSalaryFormService {
  createTblEmployeeSalaryFormGroup(tblEmployeeSalary: TblEmployeeSalaryFormGroupInput = { id: null }): TblEmployeeSalaryFormGroup {
    const tblEmployeeSalaryRawValue = this.convertTblEmployeeSalaryToTblEmployeeSalaryRawValue({
      ...this.getFormDefaults(),
      ...tblEmployeeSalary,
    });
    return new FormGroup<TblEmployeeSalaryFormGroupContent>({
      id: new FormControl(
        { value: tblEmployeeSalaryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      dateCalculated: new FormControl(tblEmployeeSalaryRawValue.dateCalculated),
      employeeId: new FormControl(tblEmployeeSalaryRawValue.employeeId),
      undertime: new FormControl(tblEmployeeSalaryRawValue.undertime),
      overtime: new FormControl(tblEmployeeSalaryRawValue.overtime),
      overtimeOffday: new FormControl(tblEmployeeSalaryRawValue.overtimeOffday),
      totalSalary: new FormControl(tblEmployeeSalaryRawValue.totalSalary),
    });
  }

  getTblEmployeeSalary(form: TblEmployeeSalaryFormGroup): ITblEmployeeSalary | NewTblEmployeeSalary {
    return this.convertTblEmployeeSalaryRawValueToTblEmployeeSalary(
      form.getRawValue() as TblEmployeeSalaryFormRawValue | NewTblEmployeeSalaryFormRawValue
    );
  }

  resetForm(form: TblEmployeeSalaryFormGroup, tblEmployeeSalary: TblEmployeeSalaryFormGroupInput): void {
    const tblEmployeeSalaryRawValue = this.convertTblEmployeeSalaryToTblEmployeeSalaryRawValue({
      ...this.getFormDefaults(),
      ...tblEmployeeSalary,
    });
    form.reset(
      {
        ...tblEmployeeSalaryRawValue,
        id: { value: tblEmployeeSalaryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TblEmployeeSalaryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateCalculated: currentTime,
    };
  }

  private convertTblEmployeeSalaryRawValueToTblEmployeeSalary(
    rawTblEmployeeSalary: TblEmployeeSalaryFormRawValue | NewTblEmployeeSalaryFormRawValue
  ): ITblEmployeeSalary | NewTblEmployeeSalary {
    return {
      ...rawTblEmployeeSalary,
      dateCalculated: dayjs(rawTblEmployeeSalary.dateCalculated, DATE_TIME_FORMAT),
    };
  }

  private convertTblEmployeeSalaryToTblEmployeeSalaryRawValue(
    tblEmployeeSalary: ITblEmployeeSalary | (Partial<NewTblEmployeeSalary> & TblEmployeeSalaryFormDefaults)
  ): TblEmployeeSalaryFormRawValue | PartialWithRequiredKeyOf<NewTblEmployeeSalaryFormRawValue> {
    return {
      ...tblEmployeeSalary,
      dateCalculated: tblEmployeeSalary.dateCalculated ? tblEmployeeSalary.dateCalculated.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
