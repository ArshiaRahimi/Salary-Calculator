import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITblSalaryInfo } from '../tbl-salary-info.model';
import { TblSalaryInfoService } from '../service/tbl-salary-info.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './tbl-salary-info-delete-dialog.component.html',
})
export class TblSalaryInfoDeleteDialogComponent {
  tblSalaryInfo?: ITblSalaryInfo;

  constructor(protected tblSalaryInfoService: TblSalaryInfoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tblSalaryInfoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
