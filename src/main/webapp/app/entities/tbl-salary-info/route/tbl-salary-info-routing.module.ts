import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TblSalaryInfoComponent } from '../list/tbl-salary-info.component';
import { TblSalaryInfoDetailComponent } from '../detail/tbl-salary-info-detail.component';
import { TblSalaryInfoUpdateComponent } from '../update/tbl-salary-info-update.component';
import { TblSalaryInfoRoutingResolveService } from './tbl-salary-info-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const tblSalaryInfoRoute: Routes = [
  {
    path: '',
    component: TblSalaryInfoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TblSalaryInfoDetailComponent,
    resolve: {
      tblSalaryInfo: TblSalaryInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TblSalaryInfoUpdateComponent,
    resolve: {
      tblSalaryInfo: TblSalaryInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TblSalaryInfoUpdateComponent,
    resolve: {
      tblSalaryInfo: TblSalaryInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tblSalaryInfoRoute)],
  exports: [RouterModule],
})
export class TblSalaryInfoRoutingModule {}
