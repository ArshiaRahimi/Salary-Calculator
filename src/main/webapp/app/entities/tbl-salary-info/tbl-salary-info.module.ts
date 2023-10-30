import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TblSalaryInfoComponent } from './list/tbl-salary-info.component';
import { TblSalaryInfoDetailComponent } from './detail/tbl-salary-info-detail.component';
import { TblSalaryInfoUpdateComponent } from './update/tbl-salary-info-update.component';
import { TblSalaryInfoDeleteDialogComponent } from './delete/tbl-salary-info-delete-dialog.component';
import { TblSalaryInfoRoutingModule } from './route/tbl-salary-info-routing.module';

@NgModule({
  imports: [SharedModule, TblSalaryInfoRoutingModule],
  declarations: [TblSalaryInfoComponent, TblSalaryInfoDetailComponent, TblSalaryInfoUpdateComponent, TblSalaryInfoDeleteDialogComponent],
})
export class TblSalaryInfoModule {}
