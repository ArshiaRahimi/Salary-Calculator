import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITblEmployeeSalary } from '../tbl-employee-salary.model';
import { TblEmployeeSalaryService } from '../service/tbl-employee-salary.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './tbl-employee-salary-delete-dialog.component.html',
})
export class TblEmployeeSalaryDeleteDialogComponent {
  tblEmployeeSalary?: ITblEmployeeSalary;

  constructor(protected tblEmployeeSalaryService: TblEmployeeSalaryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tblEmployeeSalaryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
