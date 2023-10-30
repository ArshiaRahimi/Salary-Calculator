import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TblUnauthorizedActivityComponent } from './list/tbl-unauthorized-activity.component';
import { TblUnauthorizedActivityDetailComponent } from './detail/tbl-unauthorized-activity-detail.component';
import { TblUnauthorizedActivityUpdateComponent } from './update/tbl-unauthorized-activity-update.component';
import { TblUnauthorizedActivityDeleteDialogComponent } from './delete/tbl-unauthorized-activity-delete-dialog.component';
import { TblUnauthorizedActivityRoutingModule } from './route/tbl-unauthorized-activity-routing.module';

@NgModule({
  imports: [SharedModule, TblUnauthorizedActivityRoutingModule],
  declarations: [
    TblUnauthorizedActivityComponent,
    TblUnauthorizedActivityDetailComponent,
    TblUnauthorizedActivityUpdateComponent,
    TblUnauthorizedActivityDeleteDialogComponent,
  ],
})
export class TblUnauthorizedActivityModule {}
