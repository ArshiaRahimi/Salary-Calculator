jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { TblSalaryInfoService } from '../service/tbl-salary-info.service';

import { TblSalaryInfoDeleteDialogComponent } from './tbl-salary-info-delete-dialog.component';

describe('TblSalaryInfo Management Delete Component', () => {
  let comp: TblSalaryInfoDeleteDialogComponent;
  let fixture: ComponentFixture<TblSalaryInfoDeleteDialogComponent>;
  let service: TblSalaryInfoService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TblSalaryInfoDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(TblSalaryInfoDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TblSalaryInfoDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TblSalaryInfoService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
