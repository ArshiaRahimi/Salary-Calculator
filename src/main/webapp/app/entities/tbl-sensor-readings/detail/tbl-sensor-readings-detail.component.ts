import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITblSensorReadings } from '../tbl-sensor-readings.model';

@Component({
  selector: 'jhi-tbl-sensor-readings-detail',
  templateUrl: './tbl-sensor-readings-detail.component.html',
})
export class TblSensorReadingsDetailComponent implements OnInit {
  tblSensorReadings: ITblSensorReadings | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tblSensorReadings }) => {
      this.tblSensorReadings = tblSensorReadings;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
