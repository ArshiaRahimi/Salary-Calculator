import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TblEmployeeInformationComponent } from './list/tbl-employee-information.component';
import { TblEmployeeInformationDetailComponent } from './detail/tbl-employee-information-detail.component';
import { TblEmployeeInformationUpdateComponent } from './update/tbl-employee-information-update.component';
import { TblEmployeeInformationDeleteDialogComponent } from './delete/tbl-employee-information-delete-dialog.component';
import { TblEmployeeInformationRoutingModule } from './route/tbl-employee-information-routing.module';

@NgModule({
  imports: [SharedModule, TblEmployeeInformationRoutingModule],
  declarations: [
    TblEmployeeInformationComponent,
    TblEmployeeInformationDetailComponent,
    TblEmployeeInformationUpdateComponent,
    TblEmployeeInformationDeleteDialogComponent,
  ],
})
export class TblEmployeeInformationModule {}
