import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITblSalaryInfo } from '../tbl-salary-info.model';

@Component({
  selector: 'jhi-tbl-salary-info-detail',
  templateUrl: './tbl-salary-info-detail.component.html',
})
export class TblSalaryInfoDetailComponent implements OnInit {
  tblSalaryInfo: ITblSalaryInfo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tblSalaryInfo }) => {
      this.tblSalaryInfo = tblSalaryInfo;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
