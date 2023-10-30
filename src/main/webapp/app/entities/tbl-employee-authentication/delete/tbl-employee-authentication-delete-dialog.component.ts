import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITblEmployeeAuthentication } from '../tbl-employee-authentication.model';
import { TblEmployeeAuthenticationService } from '../service/tbl-employee-authentication.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './tbl-employee-authentication-delete-dialog.component.html',
})
export class TblEmployeeAuthenticationDeleteDialogComponent {
  tblEmployeeAuthentication?: ITblEmployeeAuthentication;

  constructor(protected tblEmployeeAuthenticationService: TblEmployeeAuthenticationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tblEmployeeAuthenticationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
