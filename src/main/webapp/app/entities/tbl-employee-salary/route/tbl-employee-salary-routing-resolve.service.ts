import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITblEmployeeSalary } from '../tbl-employee-salary.model';
import { TblEmployeeSalaryService } from '../service/tbl-employee-salary.service';

@Injectable({ providedIn: 'root' })
export class TblEmployeeSalaryRoutingResolveService implements Resolve<ITblEmployeeSalary | null> {
  constructor(protected service: TblEmployeeSalaryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITblEmployeeSalary | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tblEmployeeSalary: HttpResponse<ITblEmployeeSalary>) => {
          if (tblEmployeeSalary.body) {
            return of(tblEmployeeSalary.body);
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
