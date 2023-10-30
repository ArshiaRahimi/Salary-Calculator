import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITblEmployeeInformation } from '../tbl-employee-information.model';
import { TblEmployeeInformationService } from '../service/tbl-employee-information.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './tbl-employee-information-delete-dialog.component.html',
})
export class TblEmployeeInformationDeleteDialogComponent {
  tblEmployeeInformation?: ITblEmployeeInformation;

  constructor(protected tblEmployeeInformationService: TblEmployeeInformationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tblEmployeeInformationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
