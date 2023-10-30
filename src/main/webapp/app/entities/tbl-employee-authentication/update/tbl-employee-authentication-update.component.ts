import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TblEmployeeAuthenticationFormService, TblEmployeeAuthenticationFormGroup } from './tbl-employee-authentication-form.service';
import { ITblEmployeeAuthentication } from '../tbl-employee-authentication.model';
import { TblEmployeeAuthenticationService } from '../service/tbl-employee-authentication.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-tbl-employee-authentication-update',
  templateUrl: './tbl-employee-authentication-update.component.html',
})
export class TblEmployeeAuthenticationUpdateComponent implements OnInit {
  isSaving = false;
  tblEmployeeAuthentication: ITblEmployeeAuthentication | null = null;

  editForm: TblEmployeeAuthenticationFormGroup = this.tblEmployeeAuthenticationFormService.createTblEmployeeAuthenticationFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected tblEmployeeAuthenticationService: TblEmployeeAuthenticationService,
    protected tblEmployeeAuthenticationFormService: TblEmployeeAuthenticationFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tblEmployeeAuthentication }) => {
      this.tblEmployeeAuthentication = tblEmployeeAuthentication;
      if (tblEmployeeAuthentication) {
        this.updateForm(tblEmployeeAuthentication);
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
    const tblEmployeeAuthentication = this.tblEmployeeAuthenticationFormService.getTblEmployeeAuthentication(this.editForm);
    if (tblEmployeeAuthentication.id !== null) {
      this.subscribeToSaveResponse(this.tblEmployeeAuthenticationService.update(tblEmployeeAuthentication));
    } else {
      this.subscribeToSaveResponse(this.tblEmployeeAuthenticationService.create(tblEmployeeAuthentication));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITblEmployeeAuthentication>>): void {
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

  protected updateForm(tblEmployeeAuthentication: ITblEmployeeAuthentication): void {
    this.tblEmployeeAuthentication = tblEmployeeAuthentication;
    this.tblEmployeeAuthenticationFormService.resetForm(this.editForm, tblEmployeeAuthentication);
  }
}
