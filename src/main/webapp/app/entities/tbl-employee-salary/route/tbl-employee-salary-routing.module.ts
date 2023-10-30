import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TblEmployeeSalaryComponent } from '../list/tbl-employee-salary.component';
import { TblEmployeeSalaryDetailComponent } from '../detail/tbl-employee-salary-detail.component';
import { TblEmployeeSalaryUpdateComponent } from '../update/tbl-employee-salary-update.component';
import { TblEmployeeSalaryRoutingResolveService } from './tbl-employee-salary-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const tblEmployeeSalaryRoute: Routes = [
  {
    path: '',
    component: TblEmployeeSalaryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TblEmployeeSalaryDetailComponent,
    resolve: {
      tblEmployeeSalary: TblEmployeeSalaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TblEmployeeSalaryUpdateComponent,
    resolve: {
      tblEmployeeSalary: TblEmployeeSalaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TblEmployeeSalaryUpdateComponent,
    resolve: {
      tblEmployeeSalary: TblEmployeeSalaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tblEmployeeSalaryRoute)],
  exports: [RouterModule],
})
export class TblEmployeeSalaryRoutingModule {}
