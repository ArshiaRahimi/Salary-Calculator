import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITblEmployeeInformation, NewTblEmployeeInformation } from '../tbl-employee-information.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITblEmployeeInformation for edit and NewTblEmployeeInformationFormGroupInput for create.
 */
type TblEmployeeInformationFormGroupInput = ITblEmployeeInformation | PartialWithRequiredKeyOf<NewTblEmployeeInformation>;

type TblEmployeeInformationFormDefaults = Pick<NewTblEmployeeInformation, 'id'>;

type TblEmployeeInformationFormGroupContent = {
  id: FormControl<ITblEmployeeInformation['id'] | NewTblEmployeeInformation['id']>;
  name: FormControl<ITblEmployeeInformation['name']>;
  familyName: FormControl<ITblEmployeeInformation['familyName']>;
  phoneNumber: FormControl<ITblEmployeeInformation['phoneNumber']>;
};

export type TblEmployeeInformationFormGroup = FormGroup<TblEmployeeInformationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TblEmployeeInformationFormService {
  createTblEmployeeInformationFormGroup(
    tblEmployeeInformation: TblEmployeeInformationFormGroupInput = { id: null }
  ): TblEmployeeInformationFormGroup {
    const tblEmployeeInformationRawValue = {
      ...this.getFormDefaults(),
      ...tblEmployeeInformation,
    };
    return new FormGroup<TblEmployeeInformationFormGroupContent>({
      id: new FormControl(
        { value: tblEmployeeInformationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(tblEmployeeInformationRawValue.name),
      familyName: new FormControl(tblEmployeeInformationRawValue.familyName),
      phoneNumber: new FormControl(tblEmployeeInformationRawValue.phoneNumber),
    });
  }

  getTblEmployeeInformation(form: TblEmployeeInformationFormGroup): ITblEmployeeInformation | NewTblEmployeeInformation {
    return form.getRawValue() as ITblEmployeeInformation | NewTblEmployeeInformation;
  }

  resetForm(form: TblEmployeeInformationFormGroup, tblEmployeeInformation: TblEmployeeInformationFormGroupInput): void {
    const tblEmployeeInformationRawValue = { ...this.getFormDefaults(), ...tblEmployeeInformation };
    form.reset(
      {
        ...tblEmployeeInformationRawValue,
        id: { value: tblEmployeeInformationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TblEmployeeInformationFormDefaults {
    return {
      id: null,
    };
  }
}
