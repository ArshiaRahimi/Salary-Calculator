import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TblOffDayComponent } from './list/tbl-off-day.component';
import { TblOffDayDetailComponent } from './detail/tbl-off-day-detail.component';
import { TblOffDayUpdateComponent } from './update/tbl-off-day-update.component';
import { TblOffDayDeleteDialogComponent } from './delete/tbl-off-day-delete-dialog.component';
import { TblOffDayRoutingModule } from './route/tbl-off-day-routing.module';

@NgModule({
  imports: [SharedModule, TblOffDayRoutingModule],
  declarations: [TblOffDayComponent, TblOffDayDetailComponent, TblOffDayUpdateComponent, TblOffDayDeleteDialogComponent],
})
export class TblOffDayModule {}
