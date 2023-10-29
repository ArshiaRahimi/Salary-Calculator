import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TblSensorReadingsFormService, TblSensorReadingsFormGroup } from './tbl-sensor-readings-form.service';
import { ITblSensorReadings } from '../tbl-sensor-readings.model';
import { TblSensorReadingsService } from '../service/tbl-sensor-readings.service';

@Component({
  selector: 'jhi-tbl-sensor-readings-update',
  templateUrl: './tbl-sensor-readings-update.component.html',
})
export class TblSensorReadingsUpdateComponent implements OnInit {
  isSaving = false;
  tblSensorReadings: ITblSensorReadings | null = null;

  editForm: TblSensorReadingsFormGroup = this.tblSensorReadingsFormService.createTblSensorReadingsFormGroup();

  constructor(
    protected tblSensorReadingsService: TblSensorReadingsService,
    protected tblSensorReadingsFormService: TblSensorReadingsFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tblSensorReadings }) => {
      this.tblSensorReadings = tblSensorReadings;
      if (tblSensorReadings) {
        this.updateForm(tblSensorReadings);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tblSensorReadings = this.tblSensorReadingsFormService.getTblSensorReadings(this.editForm);
    if (tblSensorReadings.id !== null) {
      this.subscribeToSaveResponse(this.tblSensorReadingsService.update(tblSensorReadings));
    } else {
      this.subscribeToSaveResponse(this.tblSensorReadingsService.create(tblSensorReadings));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITblSensorReadings>>): void {
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

  protected updateForm(tblSensorReadings: ITblSensorReadings): void {
    this.tblSensorReadings = tblSensorReadings;
    this.tblSensorReadingsFormService.resetForm(this.editForm, tblSensorReadings);
  }
}
