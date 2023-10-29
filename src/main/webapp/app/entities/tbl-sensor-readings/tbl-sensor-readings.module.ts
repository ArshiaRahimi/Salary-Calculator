import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TblSensorReadingsComponent } from './list/tbl-sensor-readings.component';
import { TblSensorReadingsDetailComponent } from './detail/tbl-sensor-readings-detail.component';
import { TblSensorReadingsUpdateComponent } from './update/tbl-sensor-readings-update.component';
import { TblSensorReadingsDeleteDialogComponent } from './delete/tbl-sensor-readings-delete-dialog.component';
import { TblSensorReadingsRoutingModule } from './route/tbl-sensor-readings-routing.module';

@NgModule({
  imports: [SharedModule, TblSensorReadingsRoutingModule],
  declarations: [
    TblSensorReadingsComponent,
    TblSensorReadingsDetailComponent,
    TblSensorReadingsUpdateComponent,
    TblSensorReadingsDeleteDialogComponent,
  ],
})
export class TblSensorReadingsModule {}
