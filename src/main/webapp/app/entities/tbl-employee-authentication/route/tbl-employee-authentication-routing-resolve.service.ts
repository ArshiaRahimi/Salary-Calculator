import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITblEmployeeAuthentication } from '../tbl-employee-authentication.model';
import { TblEmployeeAuthenticationService } from '../service/tbl-employee-authentication.service';

@Injectable({ providedIn: 'root' })
export class TblEmployeeAuthenticationRoutingResolveService implements Resolve<ITblEmployeeAuthentication | null> {
  constructor(protected service: TblEmployeeAuthenticationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITblEmployeeAuthentication | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tblEmployeeAuthentication: HttpResponse<ITblEmployeeAuthentication>) => {
          if (tblEmployeeAuthentication.body) {
            return of(tblEmployeeAuthentication.body);
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
