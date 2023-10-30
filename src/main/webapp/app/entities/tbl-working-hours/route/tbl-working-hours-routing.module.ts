import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TblWorkingHoursComponent } from '../list/tbl-working-hours.component';
import { TblWorkingHoursDetailComponent } from '../detail/tbl-working-hours-detail.component';
import { TblWorkingHoursUpdateComponent } from '../update/tbl-working-hours-update.component';
import { TblWorkingHoursRoutingResolveService } from './tbl-working-hours-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const tblWorkingHoursRoute: Routes = [
  {
    path: '',
    component: TblWorkingHoursComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TblWorkingHoursDetailComponent,
    resolve: {
      tblWorkingHours: TblWorkingHoursRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TblWorkingHoursUpdateComponent,
    resolve: {
      tblWorkingHours: TblWorkingHoursRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TblWorkingHoursUpdateComponent,
    resolve: {
      tblWorkingHours: TblWorkingHoursRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tblWorkingHoursRoute)],
  exports: [RouterModule],
})
export class TblWorkingHoursRoutingModule {}
