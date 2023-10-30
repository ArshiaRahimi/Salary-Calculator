import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITblOffDay } from '../tbl-off-day.model';
import { TblOffDayService } from '../service/tbl-off-day.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './tbl-off-day-delete-dialog.component.html',
})
export class TblOffDayDeleteDialogComponent {
  tblOffDay?: ITblOffDay;

  constructor(protected tblOffDayService: TblOffDayService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tblOffDayService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
