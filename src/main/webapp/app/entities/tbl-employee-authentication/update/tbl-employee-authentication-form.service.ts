import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITblEmployeeAuthentication, NewTblEmployeeAuthentication } from '../tbl-employee-authentication.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITblEmployeeAuthentication for edit and NewTblEmployeeAuthenticationFormGroupInput for create.
 */
type TblEmployeeAuthenticationFormGroupInput = ITblEmployeeAuthentication | PartialWithRequiredKeyOf<NewTblEmployeeAuthentication>;

type TblEmployeeAuthenticationFormDefaults = Pick<NewTblEmployeeAuthentication, 'id'>;

type TblEmployeeAuthenticationFormGroupContent = {
  id: FormControl<ITblEmployeeAuthentication['id'] | NewTblEmployeeAuthentication['id']>;
  employeeId: FormControl<ITblEmployeeAuthentication['employeeId']>;
  rfidId: FormControl<ITblEmployeeAuthentication['rfidId']>;
  fingerprint: FormControl<ITblEmployeeAuthentication['fingerprint']>;
  isActive: FormControl<ITblEmployeeAuthentication['isActive']>;
};

export type TblEmployeeAuthenticationFormGroup = FormGroup<TblEmployeeAuthenticationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TblEmployeeAuthenticationFormService {
  createTblEmployeeAuthenticationFormGroup(
    tblEmployeeAuthentication: TblEmployeeAuthenticationFormGroupInput = { id: null }
  ): TblEmployeeAuthenticationFormGroup {
    const tblEmployeeAuthenticationRawValue = {
      ...this.getFormDefaults(),
      ...tblEmployeeAuthentication,
    };
    return new FormGroup<TblEmployeeAuthenticationFormGroupContent>({
      id: new FormControl(
        { value: tblEmployeeAuthenticationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      employeeId: new FormControl(tblEmployeeAuthenticationRawValue.employeeId),
      rfidId: new FormControl(tblEmployeeAuthenticationRawValue.rfidId),
      fingerprint: new FormControl(tblEmployeeAuthenticationRawValue.fingerprint),
      isActive: new FormControl(tblEmployeeAuthenticationRawValue.isActive),
    });
  }

  getTblEmployeeAuthentication(form: TblEmployeeAuthenticationFormGroup): ITblEmployeeAuthentication | NewTblEmployeeAuthentication {
    return form.getRawValue() as ITblEmployeeAuthentication | NewTblEmployeeAuthentication;
  }

  resetForm(form: TblEmployeeAuthenticationFormGroup, tblEmployeeAuthentication: TblEmployeeAuthenticationFormGroupInput): void {
    const tblEmployeeAuthenticationRawValue = { ...this.getFormDefaults(), ...tblEmployeeAuthentication };
    form.reset(
      {
        ...tblEmployeeAuthenticationRawValue,
        id: { value: tblEmployeeAuthenticationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TblEmployeeAuthenticationFormDefaults {
    return {
      id: null,
    };
  }
}
