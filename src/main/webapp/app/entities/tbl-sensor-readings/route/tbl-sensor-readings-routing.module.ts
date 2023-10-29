import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TblSensorReadingsComponent } from '../list/tbl-sensor-readings.component';
import { TblSensorReadingsDetailComponent } from '../detail/tbl-sensor-readings-detail.component';
import { TblSensorReadingsUpdateComponent } from '../update/tbl-sensor-readings-update.component';
import { TblSensorReadingsRoutingResolveService } from './tbl-sensor-readings-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const tblSensorReadingsRoute: Routes = [
  {
    path: '',
    component: TblSensorReadingsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TblSensorReadingsDetailComponent,
    resolve: {
      tblSensorReadings: TblSensorReadingsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TblSensorReadingsUpdateComponent,
    resolve: {
      tblSensorReadings: TblSensorReadingsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TblSensorReadingsUpdateComponent,
    resolve: {
      tblSensorReadings: TblSensorReadingsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tblSensorReadingsRoute)],
  exports: [RouterModule],
})
export class TblSensorReadingsRoutingModule {}
