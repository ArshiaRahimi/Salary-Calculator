import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITblWorkingHours } from '../tbl-working-hours.model';
import { TblWorkingHoursService } from '../service/tbl-working-hours.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './tbl-working-hours-delete-dialog.component.html',
})
export class TblWorkingHoursDeleteDialogComponent {
  tblWorkingHours?: ITblWorkingHours;

  constructor(protected tblWorkingHoursService: TblWorkingHoursService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tblWorkingHoursService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
