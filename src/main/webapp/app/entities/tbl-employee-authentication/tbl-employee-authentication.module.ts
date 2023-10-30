import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TblEmployeeAuthenticationComponent } from './list/tbl-employee-authentication.component';
import { TblEmployeeAuthenticationDetailComponent } from './detail/tbl-employee-authentication-detail.component';
import { TblEmployeeAuthenticationUpdateComponent } from './update/tbl-employee-authentication-update.component';
import { TblEmployeeAuthenticationDeleteDialogComponent } from './delete/tbl-employee-authentication-delete-dialog.component';
import { TblEmployeeAuthenticationRoutingModule } from './route/tbl-employee-authentication-routing.module';

@NgModule({
  imports: [SharedModule, TblEmployeeAuthenticationRoutingModule],
  declarations: [
    TblEmployeeAuthenticationComponent,
    TblEmployeeAuthenticationDetailComponent,
    TblEmployeeAuthenticationUpdateComponent,
    TblEmployeeAuthenticationDeleteDialogComponent,
  ],
})
export class TblEmployeeAuthenticationModule {}
