import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITblSalaryInfo } from '../tbl-salary-info.model';
import { TblSalaryInfoService } from '../service/tbl-salary-info.service';

@Injectable({ providedIn: 'root' })
export class TblSalaryInfoRoutingResolveService implements Resolve<ITblSalaryInfo | null> {
  constructor(protected service: TblSalaryInfoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITblSalaryInfo | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tblSalaryInfo: HttpResponse<ITblSalaryInfo>) => {
          if (tblSalaryInfo.body) {
            return of(tblSalaryInfo.body);
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
