import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TblEmployeeInformationComponent } from '../list/tbl-employee-information.component';
import { TblEmployeeInformationDetailComponent } from '../detail/tbl-employee-information-detail.component';
import { TblEmployeeInformationUpdateComponent } from '../update/tbl-employee-information-update.component';
import { TblEmployeeInformationRoutingResolveService } from './tbl-employee-information-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const tblEmployeeInformationRoute: Routes = [
  {
    path: '',
    component: TblEmployeeInformationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TblEmployeeInformationDetailComponent,
    resolve: {
      tblEmployeeInformation: TblEmployeeInformationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TblEmployeeInformationUpdateComponent,
    resolve: {
      tblEmployeeInformation: TblEmployeeInformationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TblEmployeeInformationUpdateComponent,
    resolve: {
      tblEmployeeInformation: TblEmployeeInformationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tblEmployeeInformationRoute)],
  exports: [RouterModule],
})
export class TblEmployeeInformationRoutingModule {}
