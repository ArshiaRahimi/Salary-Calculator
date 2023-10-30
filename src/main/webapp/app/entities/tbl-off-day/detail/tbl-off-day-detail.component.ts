import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITblOffDay } from '../tbl-off-day.model';

@Component({
  selector: 'jhi-tbl-off-day-detail',
  templateUrl: './tbl-off-day-detail.component.html',
})
export class TblOffDayDetailComponent implements OnInit {
  tblOffDay: ITblOffDay | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tblOffDay }) => {
      this.tblOffDay = tblOffDay;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
