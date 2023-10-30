import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TblSalaryInfoDetailComponent } from './tbl-salary-info-detail.component';

describe('TblSalaryInfo Management Detail Component', () => {
  let comp: TblSalaryInfoDetailComponent;
  let fixture: ComponentFixture<TblSalaryInfoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TblSalaryInfoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tblSalaryInfo: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TblSalaryInfoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TblSalaryInfoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tblSalaryInfo on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tblSalaryInfo).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
