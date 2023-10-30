import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TblEmployeeSalaryDetailComponent } from './tbl-employee-salary-detail.component';

describe('TblEmployeeSalary Management Detail Component', () => {
  let comp: TblEmployeeSalaryDetailComponent;
  let fixture: ComponentFixture<TblEmployeeSalaryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TblEmployeeSalaryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tblEmployeeSalary: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TblEmployeeSalaryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TblEmployeeSalaryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tblEmployeeSalary on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tblEmployeeSalary).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
