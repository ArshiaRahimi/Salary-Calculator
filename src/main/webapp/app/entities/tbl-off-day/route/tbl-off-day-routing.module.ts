import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TblOffDayComponent } from '../list/tbl-off-day.component';
import { TblOffDayDetailComponent } from '../detail/tbl-off-day-detail.component';
import { TblOffDayUpdateComponent } from '../update/tbl-off-day-update.component';
import { TblOffDayRoutingResolveService } from './tbl-off-day-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const tblOffDayRoute: Routes = [
  {
    path: '',
    component: TblOffDayComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TblOffDayDetailComponent,
    resolve: {
      tblOffDay: TblOffDayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TblOffDayUpdateComponent,
    resolve: {
      tblOffDay: TblOffDayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TblOffDayUpdateComponent,
    resolve: {
      tblOffDay: TblOffDayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tblOffDayRoute)],
  exports: [RouterModule],
})
export class TblOffDayRoutingModule {}
