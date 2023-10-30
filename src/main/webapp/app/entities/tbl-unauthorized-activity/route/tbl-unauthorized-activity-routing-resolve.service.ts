import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITblUnauthorizedActivity } from '../tbl-unauthorized-activity.model';
import { TblUnauthorizedActivityService } from '../service/tbl-unauthorized-activity.service';

@Injectable({ providedIn: 'root' })
export class TblUnauthorizedActivityRoutingResolveService implements Resolve<ITblUnauthorizedActivity | null> {
  constructor(protected service: TblUnauthorizedActivityService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITblUnauthorizedActivity | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tblUnauthorizedActivity: HttpResponse<ITblUnauthorizedActivity>) => {
          if (tblUnauthorizedActivity.body) {
            return of(tblUnauthorizedActivity.body);
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
