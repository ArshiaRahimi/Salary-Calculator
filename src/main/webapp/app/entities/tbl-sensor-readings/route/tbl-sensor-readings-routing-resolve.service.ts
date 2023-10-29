import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITblSensorReadings } from '../tbl-sensor-readings.model';
import { TblSensorReadingsService } from '../service/tbl-sensor-readings.service';

@Injectable({ providedIn: 'root' })
export class TblSensorReadingsRoutingResolveService implements Resolve<ITblSensorReadings | null> {
  constructor(protected service: TblSensorReadingsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITblSensorReadings | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tblSensorReadings: HttpResponse<ITblSensorReadings>) => {
          if (tblSensorReadings.body) {
            return of(tblSensorReadings.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
