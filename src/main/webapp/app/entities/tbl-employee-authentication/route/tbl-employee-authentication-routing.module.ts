import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TblEmployeeAuthenticationComponent } from '../list/tbl-employee-authentication.component';
import { TblEmployeeAuthenticationDetailComponent } from '../detail/tbl-employee-authentication-detail.component';
import { TblEmployeeAuthenticationUpdateComponent } from '../update/tbl-employee-authentication-update.component';
import { TblEmployeeAuthenticationRoutingResolveService } from './tbl-employee-authentication-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const tblEmployeeAuthenticationRoute: Routes = [
  {
    path: '',
    component: TblEmployeeAuthenticationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TblEmployeeAuthenticationDetailComponent,
    resolve: {
      tblEmployeeAuthentication: TblEmployeeAuthenticationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TblEmployeeAuthenticationUpdateComponent,
    resolve: {
      tblEmployeeAuthentication: TblEmployeeAuthenticationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TblEmployeeAuthenticationUpdateComponent,
    resolve: {
      tblEmployeeAuthentication: TblEmployeeAuthenticationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tblEmployeeAuthenticationRoute)],
  exports: [RouterModule],
})
export class TblEmployeeAuthenticationRoutingModule {}
