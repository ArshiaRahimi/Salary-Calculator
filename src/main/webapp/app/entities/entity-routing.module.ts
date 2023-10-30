import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'tbl-sensor-readings',
        data: { pageTitle: 'projectApp.tblSensorReadings.home.title' },
        loadChildren: () => import('./tbl-sensor-readings/tbl-sensor-readings.module').then(m => m.TblSensorReadingsModule),
      },
      {
        path: 'tbl-working-hours',
        data: { pageTitle: 'projectApp.tblWorkingHours.home.title' },
        loadChildren: () => import('./tbl-working-hours/tbl-working-hours.module').then(m => m.TblWorkingHoursModule),
      },
      {
        path: 'tbl-off-day',
        data: { pageTitle: 'projectApp.tblOffDay.home.title' },
        loadChildren: () => import('./tbl-off-day/tbl-off-day.module').then(m => m.TblOffDayModule),
      },
      {
        path: 'tbl-unauthorized-activity',
        data: { pageTitle: 'projectApp.tblUnauthorizedActivity.home.title' },
        loadChildren: () =>
          import('./tbl-unauthorized-activity/tbl-unauthorized-activity.module').then(m => m.TblUnauthorizedActivityModule),
      },
      {
        path: 'tbl-employee-information',
        data: { pageTitle: 'projectApp.tblEmployeeInformation.home.title' },
        loadChildren: () => import('./tbl-employee-information/tbl-employee-information.module').then(m => m.TblEmployeeInformationModule),
      },
      {
        path: 'tbl-salary-info',
        data: { pageTitle: 'projectApp.tblSalaryInfo.home.title' },
        loadChildren: () => import('./tbl-salary-info/tbl-salary-info.module').then(m => m.TblSalaryInfoModule),
      },
      {
        path: 'tbl-employee-authentication',
        data: { pageTitle: 'projectApp.tblEmployeeAuthentication.home.title' },
        loadChildren: () =>
          import('./tbl-employee-authentication/tbl-employee-authentication.module').then(m => m.TblEmployeeAuthenticationModule),
      },
      {
        path: 'tbl-employee-salary',
        data: { pageTitle: 'projectApp.tblEmployeeSalary.home.title' },
        loadChildren: () => import('./tbl-employee-salary/tbl-employee-salary.module').then(m => m.TblEmployeeSalaryModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
