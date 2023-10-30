import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITblSalaryInfo, NewTblSalaryInfo } from '../tbl-salary-info.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITblSalaryInfo for edit and NewTblSalaryInfoFormGroupInput for create.
 */
type TblSalaryInfoFormGroupInput = ITblSalaryInfo | PartialWithRequiredKeyOf<NewTblSalaryInfo>;

type TblSalaryInfoFormDefaults = Pick<NewTblSalaryInfo, 'id'>;

type TblSalaryInfoFormGroupContent = {
  id: FormControl<ITblSalaryInfo['id'] | NewTblSalaryInfo['id']>;
  employeeId: FormControl<ITblSalaryInfo['employeeId']>;
  baseSalary: FormControl<ITblSalaryInfo['baseSalary']>;
  housingRights: FormControl<ITblSalaryInfo['housingRights']>;
  internetRights: FormControl<ITblSalaryInfo['internetRights']>;
  groceriesRights: FormControl<ITblSalaryInfo['groceriesRights']>;
};

export type TblSalaryInfoFormGroup = FormGroup<TblSalaryInfoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TblSalaryInfoFormService {
  createTblSalaryInfoFormGroup(tblSalaryInfo: TblSalaryInfoFormGroupInput = { id: null }): TblSalaryInfoFormGroup {
    const tblSalaryInfoRawValue = {
      ...this.getFormDefaults(),
      ...tblSalaryInfo,
    };
    return new FormGroup<TblSalaryInfoFormGroupContent>({
      id: new FormControl(
        { value: tblSalaryInfoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      employeeId: new FormControl(tblSalaryInfoRawValue.employeeId),
      baseSalary: new FormControl(tblSalaryInfoRawValue.baseSalary),
      housingRights: new FormControl(tblSalaryInfoRawValue.housingRights),
      internetRights: new FormControl(tblSalaryInfoRawValue.internetRights),
      groceriesRights: new FormControl(tblSalaryInfoRawValue.groceriesRights),
    });
  }

  getTblSalaryInfo(form: TblSalaryInfoFormGroup): ITblSalaryInfo | NewTblSalaryInfo {
    return form.getRawValue() as ITblSalaryInfo | NewTblSalaryInfo;
  }

  resetForm(form: TblSalaryInfoFormGroup, tblSalaryInfo: TblSalaryInfoFormGroupInput): void {
    const tblSalaryInfoRawValue = { ...this.getFormDefaults(), ...tblSalaryInfo };
    form.reset(
      {
        ...tblSalaryInfoRawValue,
        id: { value: tblSalaryInfoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TblSalaryInfoFormDefaults {
    return {
      id: null,
    };
  }
}
