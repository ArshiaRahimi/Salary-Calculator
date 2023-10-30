import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITblEmployeeSalary } from '../tbl-employee-salary.model';

@Component({
  selector: 'jhi-tbl-employee-salary-detail',
  templateUrl: './tbl-employee-salary-detail.component.html',
})
export class TblEmployeeSalaryDetailComponent implements OnInit {
  tblEmployeeSalary: ITblEmployeeSalary | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tblEmployeeSalary }) => {
      this.tblEmployeeSalary = tblEmployeeSalary;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
