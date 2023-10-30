import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TblSalaryInfoFormService, TblSalaryInfoFormGroup } from './tbl-salary-info-form.service';
import { ITblSalaryInfo } from '../tbl-salary-info.model';
import { TblSalaryInfoService } from '../service/tbl-salary-info.service';

@Component({
  selector: 'jhi-tbl-salary-info-update',
  templateUrl: './tbl-salary-info-update.component.html',
})
export class TblSalaryInfoUpdateComponent implements OnInit {
  isSaving = false;
  tblSalaryInfo: ITblSalaryInfo | null = null;

  editForm: TblSalaryInfoFormGroup = this.tblSalaryInfoFormService.createTblSalaryInfoFormGroup();

  constructor(
    protected tblSalaryInfoService: TblSalaryInfoService,
    protected tblSalaryInfoFormService: TblSalaryInfoFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tblSalaryInfo }) => {
      this.tblSalaryInfo = tblSalaryInfo;
      if (tblSalaryInfo) {
        this.updateForm(tblSalaryInfo);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tblSalaryInfo = this.tblSalaryInfoFormService.getTblSalaryInfo(this.editForm);
    if (tblSalaryInfo.id !== null) {
      this.subscribeToSaveResponse(this.tblSalaryInfoService.update(tblSalaryInfo));
    } else {
      this.subscribeToSaveResponse(this.tblSalaryInfoService.create(tblSalaryInfo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITblSalaryInfo>>): void {
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

  protected updateForm(tblSalaryInfo: ITblSalaryInfo): void {
    this.tblSalaryInfo = tblSalaryInfo;
    this.tblSalaryInfoFormService.resetForm(this.editForm, tblSalaryInfo);
  }
}
