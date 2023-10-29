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
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
