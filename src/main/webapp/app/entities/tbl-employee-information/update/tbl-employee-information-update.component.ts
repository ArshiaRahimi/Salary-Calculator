import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TblEmployeeInformationFormService, TblEmployeeInformationFormGroup } from './tbl-employee-information-form.service';
import { ITblEmployeeInformation } from '../tbl-employee-information.model';
import { TblEmployeeInformationService } from '../service/tbl-employee-information.service';

@Component({
  selector: 'jhi-tbl-employee-information-update',
  templateUrl: './tbl-employee-information-update.component.html',
})
export class TblEmployeeInformationUpdateComponent implements OnInit {
  isSaving = false;
  tblEmployeeInformation: ITblEmployeeInformation | null = null;

  editForm: TblEmployeeInformationFormGroup = this.tblEmployeeInformationFormService.createTblEmployeeInformationFormGroup();

  constructor(
    protected tblEmployeeInformationService: TblEmployeeInformationService,
    protected tblEmployeeInformationFormService: TblEmployeeInformationFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tblEmployeeInformation }) => {
      this.tblEmployeeInformation = tblEmployeeInformation;
      if (tblEmployeeInformation) {
        this.updateForm(tblEmployeeInformation);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tblEmployeeInformation = this.tblEmployeeInformationFormService.getTblEmployeeInformation(this.editForm);
    if (tblEmployeeInformation.id !== null) {
      this.subscribeToSaveResponse(this.tblEmployeeInformationService.update(tblEmployeeInformation));
    } else {
      this.subscribeToSaveResponse(this.tblEmployeeInformationService.create(tblEmployeeInformation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITblEmployeeInformation>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(tblEmployeeInformation: ITblEmployeeInformation): void {
    this.tblEmployeeInformation = tblEmployeeInformation;
    this.tblEmployeeInformationFormService.resetForm(this.editForm, tblEmployeeInformation);
  }
}
