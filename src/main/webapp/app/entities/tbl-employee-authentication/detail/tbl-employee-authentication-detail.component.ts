import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITblEmployeeAuthentication } from '../tbl-employee-authentication.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-tbl-employee-authentication-detail',
  templateUrl: './tbl-employee-authentication-detail.component.html',
})
export class TblEmployeeAuthenticationDetailComponent implements OnInit {
  tblEmployeeAuthentication: ITblEmployeeAuthentication | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tblEmployeeAuthentication }) => {
      this.tblEmployeeAuthentication = tblEmployeeAuthentication;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
