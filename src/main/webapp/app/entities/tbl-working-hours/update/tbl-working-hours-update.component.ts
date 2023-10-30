import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TblWorkingHoursFormService, TblWorkingHoursFormGroup } from './tbl-working-hours-form.service';
import { ITblWorkingHours } from '../tbl-working-hours.model';
import { TblWorkingHoursService } from '../service/tbl-working-hours.service';

@Component({
  selector: 'jhi-tbl-working-hours-update',
  templateUrl: './tbl-working-hours-update.component.html',
})
export class TblWorkingHoursUpdateComponent implements OnInit {
  isSaving = false;
  tblWorkingHours: ITblWorkingHours | null = null;

  editForm: TblWorkingHoursFormGroup = this.tblWorkingHoursFormService.createTblWorkingHoursFormGroup();

  constructor(
    protected tblWorkingHoursService: TblWorkingHoursService,
    protected tblWorkingHoursFormService: TblWorkingHoursFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tblWorkingHours }) => {
      this.tblWorkingHours = tblWorkingHours;
      if (tblWorkingHours) {
        this.updateForm(tblWorkingHours);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tblWorkingHours = this.tblWorkingHoursFormService.getTblWorkingHours(this.editForm);
    if (tblWorkingHours.id !== null) {
      this.subscribeToSaveResponse(this.tblWorkingHoursService.update(tblWorkingHours));
    } else {
      this.subscribeToSaveResponse(this.tblWorkingHoursService.create(tblWorkingHours));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITblWorkingHours>>): void {
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

  protected updateForm(tblWorkingHours: ITblWorkingHours): void {
    this.tblWorkingHours = tblWorkingHours;
    this.tblWorkingHoursFormService.resetForm(this.editForm, tblWorkingHours);
  }
}
