import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TblEmployeeSalaryFormService, TblEmployeeSalaryFormGroup } from './tbl-employee-salary-form.service';
import { ITblEmployeeSalary } from '../tbl-employee-salary.model';
import { TblEmployeeSalaryService } from '../service/tbl-employee-salary.service';

@Component({
  selector: 'jhi-tbl-employee-salary-update',
  templateUrl: './tbl-employee-salary-update.component.html',
})
export class TblEmployeeSalaryUpdateComponent implements OnInit {
  isSaving = false;
  tblEmployeeSalary: ITblEmployeeSalary | null = null;

  editForm: TblEmployeeSalaryFormGroup = this.tblEmployeeSalaryFormService.createTblEmployeeSalaryFormGroup();

  constructor(
    protected tblEmployeeSalaryService: TblEmployeeSalaryService,
    protected tblEmployeeSalaryFormService: TblEmployeeSalaryFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tblEmployeeSalary }) => {
      this.tblEmployeeSalary = tblEmployeeSalary;
      if (tblEmployeeSalary) {
        this.updateForm(tblEmployeeSalary);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tblEmployeeSalary = this.tblEmployeeSalaryFormService.getTblEmployeeSalary(this.editForm);
    if (tblEmployeeSalary.id !== null) {
      this.subscribeToSaveResponse(this.tblEmployeeSalaryService.update(tblEmployeeSalary));
    } else {
      this.subscribeToSaveResponse(this.tblEmployeeSalaryService.create(tblEmployeeSalary));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITblEmployeeSalary>>): void {
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

  protected updateForm(tblEmployeeSalary: ITblEmployeeSalary): void {
    this.tblEmployeeSalary = tblEmployeeSalary;
    this.tblEmployeeSalaryFormService.resetForm(this.editForm, tblEmployeeSalary);
  }
}
