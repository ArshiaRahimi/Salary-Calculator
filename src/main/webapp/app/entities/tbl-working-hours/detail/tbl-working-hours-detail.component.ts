import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITblWorkingHours } from '../tbl-working-hours.model';

@Component({
  selector: 'jhi-tbl-working-hours-detail',
  templateUrl: './tbl-working-hours-detail.component.html',
})
export class TblWorkingHoursDetailComponent implements OnInit {
  tblWorkingHours: ITblWorkingHours | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tblWorkingHours }) => {
      this.tblWorkingHours = tblWorkingHours;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
