import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITblUnauthorizedActivity } from '../tbl-unauthorized-activity.model';
import { TblUnauthorizedActivityService } from '../service/tbl-unauthorized-activity.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './tbl-unauthorized-activity-delete-dialog.component.html',
})
export class TblUnauthorizedActivityDeleteDialogComponent {
  tblUnauthorizedActivity?: ITblUnauthorizedActivity;

  constructor(protected tblUnauthorizedActivityService: TblUnauthorizedActivityService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tblUnauthorizedActivityService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
