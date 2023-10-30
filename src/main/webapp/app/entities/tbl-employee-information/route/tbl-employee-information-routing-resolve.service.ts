import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITblEmployeeInformation } from '../tbl-employee-information.model';
import { TblEmployeeInformationService } from '../service/tbl-employee-information.service';

@Injectable({ providedIn: 'root' })
export class TblEmployeeInformationRoutingResolveService implements Resolve<ITblEmployeeInformation | null> {
  constructor(protected service: TblEmployeeInformationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITblEmployeeInformation | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tblEmployeeInformation: HttpResponse<ITblEmployeeInformation>) => {
          if (tblEmployeeInformation.body) {
            return of(tblEmployeeInformation.body);
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
