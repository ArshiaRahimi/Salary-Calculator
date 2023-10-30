import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TblUnauthorizedActivityFormService, TblUnauthorizedActivityFormGroup } from './tbl-unauthorized-activity-form.service';
import { ITblUnauthorizedActivity } from '../tbl-unauthorized-activity.model';
import { TblUnauthorizedActivityService } from '../service/tbl-unauthorized-activity.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-tbl-unauthorized-activity-update',
  templateUrl: './tbl-unauthorized-activity-update.component.html',
})
export class TblUnauthorizedActivityUpdateComponent implements OnInit {
  isSaving = false;
  tblUnauthorizedActivity: ITblUnauthorizedActivity | null = null;

  editForm: TblUnauthorizedActivityFormGroup = this.tblUnauthorizedActivityFormService.createTblUnauthorizedActivityFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected tblUnauthorizedActivityService: TblUnauthorizedActivityService,
    protected tblUnauthorizedActivityFormService: TblUnauthorizedActivityFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tblUnauthorizedActivity }) => {
      this.tblUnauthorizedActivity = tblUnauthorizedActivity;
      if (tblUnauthorizedActivity) {
        this.updateForm(tblUnauthorizedActivity);
      }
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('projectApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tblUnauthorizedActivity = this.tblUnauthorizedActivityFormService.getTblUnauthorizedActivity(this.editForm);
    if (tblUnauthorizedActivity.id !== null) {
      this.subscribeToSaveResponse(this.tblUnauthorizedActivityService.update(tblUnauthorizedActivity));
    } else {
      this.subscribeToSaveResponse(this.tblUnauthorizedActivityService.create(tblUnauthorizedActivity));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITblUnauthorizedActivity>>): void {
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

  protected updateForm(tblUnauthorizedActivity: ITblUnauthorizedActivity): void {
    this.tblUnauthorizedActivity = tblUnauthorizedActivity;
    this.tblUnauthorizedActivityFormService.resetForm(this.editForm, tblUnauthorizedActivity);
  }
}
