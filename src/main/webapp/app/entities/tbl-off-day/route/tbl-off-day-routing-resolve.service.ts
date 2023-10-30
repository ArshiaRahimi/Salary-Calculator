import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITblOffDay } from '../tbl-off-day.model';
import { TblOffDayService } from '../service/tbl-off-day.service';

@Injectable({ providedIn: 'root' })
export class TblOffDayRoutingResolveService implements Resolve<ITblOffDay | null> {
  constructor(protected service: TblOffDayService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITblOffDay | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tblOffDay: HttpResponse<ITblOffDay>) => {
          if (tblOffDay.body) {
            return of(tblOffDay.body);
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
