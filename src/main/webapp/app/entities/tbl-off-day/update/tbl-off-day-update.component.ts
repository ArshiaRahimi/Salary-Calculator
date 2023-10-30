import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TblOffDayFormService, TblOffDayFormGroup } from './tbl-off-day-form.service';
import { ITblOffDay } from '../tbl-off-day.model';
import { TblOffDayService } from '../service/tbl-off-day.service';

@Component({
  selector: 'jhi-tbl-off-day-update',
  templateUrl: './tbl-off-day-update.component.html',
})
export class TblOffDayUpdateComponent implements OnInit {
  isSaving = false;
  tblOffDay: ITblOffDay | null = null;

  editForm: TblOffDayFormGroup = this.tblOffDayFormService.createTblOffDayFormGroup();

  constructor(
    protected tblOffDayService: TblOffDayService,
    protected tblOffDayFormService: TblOffDayFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tblOffDay }) => {
      this.tblOffDay = tblOffDay;
      if (tblOffDay) {
        this.updateForm(tblOffDay);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tblOffDay = this.tblOffDayFormService.getTblOffDay(this.editForm);
    if (tblOffDay.id !== null) {
      this.subscribeToSaveResponse(this.tblOffDayService.update(tblOffDay));
    } else {
      this.subscribeToSaveResponse(this.tblOffDayService.create(tblOffDay));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITblOffDay>>): void {
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

  protected updateForm(tblOffDay: ITblOffDay): void {
    this.tblOffDay = tblOffDay;
    this.tblOffDayFormService.resetForm(this.editForm, tblOffDay);
  }
}
