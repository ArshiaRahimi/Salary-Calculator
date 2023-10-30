import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITblEmployeeInformation } from '../tbl-employee-information.model';

@Component({
  selector: 'jhi-tbl-employee-information-detail',
  templateUrl: './tbl-employee-information-detail.component.html',
})
export class TblEmployeeInformationDetailComponent implements OnInit {
  tblEmployeeInformation: ITblEmployeeInformation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tblEmployeeInformation }) => {
      this.tblEmployeeInformation = tblEmployeeInformation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
