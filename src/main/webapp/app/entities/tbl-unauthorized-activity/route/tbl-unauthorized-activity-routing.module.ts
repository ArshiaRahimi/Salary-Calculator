import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TblUnauthorizedActivityComponent } from '../list/tbl-unauthorized-activity.component';
import { TblUnauthorizedActivityDetailComponent } from '../detail/tbl-unauthorized-activity-detail.component';
import { TblUnauthorizedActivityUpdateComponent } from '../update/tbl-unauthorized-activity-update.component';
import { TblUnauthorizedActivityRoutingResolveService } from './tbl-unauthorized-activity-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const tblUnauthorizedActivityRoute: Routes = [
  {
    path: '',
    component: TblUnauthorizedActivityComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TblUnauthorizedActivityDetailComponent,
    resolve: {
      tblUnauthorizedActivity: TblUnauthorizedActivityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TblUnauthorizedActivityUpdateComponent,
    resolve: {
      tblUnauthorizedActivity: TblUnauthorizedActivityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TblUnauthorizedActivityUpdateComponent,
    resolve: {
      tblUnauthorizedActivity: TblUnauthorizedActivityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tblUnauthorizedActivityRoute)],
  exports: [RouterModule],
})
export class TblUnauthorizedActivityRoutingModule {}
