import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITblWorkingHours } from '../tbl-working-hours.model';
import { TblWorkingHoursService } from '../service/tbl-working-hours.service';

@Injectable({ providedIn: 'root' })
export class TblWorkingHoursRoutingResolveService implements Resolve<ITblWorkingHours | null> {
  constructor(protected service: TblWorkingHoursService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITblWorkingHours | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tblWorkingHours: HttpResponse<ITblWorkingHours>) => {
          if (tblWorkingHours.body) {
            return of(tblWorkingHours.body);
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
