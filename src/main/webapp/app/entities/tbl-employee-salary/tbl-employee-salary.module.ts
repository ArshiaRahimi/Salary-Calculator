import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TblEmployeeSalaryComponent } from './list/tbl-employee-salary.component';
import { TblEmployeeSalaryDetailComponent } from './detail/tbl-employee-salary-detail.component';
import { TblEmployeeSalaryUpdateComponent } from './update/tbl-employee-salary-update.component';
import { TblEmployeeSalaryDeleteDialogComponent } from './delete/tbl-employee-salary-delete-dialog.component';
import { TblEmployeeSalaryRoutingModule } from './route/tbl-employee-salary-routing.module';

@NgModule({
  imports: [SharedModule, TblEmployeeSalaryRoutingModule],
  declarations: [
    TblEmployeeSalaryComponent,
    TblEmployeeSalaryDetailComponent,
    TblEmployeeSalaryUpdateComponent,
    TblEmployeeSalaryDeleteDialogComponent,
  ],
})
export class TblEmployeeSalaryModule {}
