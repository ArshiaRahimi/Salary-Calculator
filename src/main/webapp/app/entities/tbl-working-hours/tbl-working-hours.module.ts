import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TblWorkingHoursComponent } from './list/tbl-working-hours.component';
import { TblWorkingHoursDetailComponent } from './detail/tbl-working-hours-detail.component';
import { TblWorkingHoursUpdateComponent } from './update/tbl-working-hours-update.component';
import { TblWorkingHoursDeleteDialogComponent } from './delete/tbl-working-hours-delete-dialog.component';
import { TblWorkingHoursRoutingModule } from './route/tbl-working-hours-routing.module';

@NgModule({
  imports: [SharedModule, TblWorkingHoursRoutingModule],
  declarations: [
    TblWorkingHoursComponent,
    TblWorkingHoursDetailComponent,
    TblWorkingHoursUpdateComponent,
    TblWorkingHoursDeleteDialogComponent,
  ],
})
export class TblWorkingHoursModule {}
