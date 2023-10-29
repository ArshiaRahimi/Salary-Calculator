import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITblSensorReadings } from '../tbl-sensor-readings.model';
import { TblSensorReadingsService } from '../service/tbl-sensor-readings.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './tbl-sensor-readings-delete-dialog.component.html',
})
export class TblSensorReadingsDeleteDialogComponent {
  tblSensorReadings?: ITblSensorReadings;

  constructor(protected tblSensorReadingsService: TblSensorReadingsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tblSensorReadingsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
