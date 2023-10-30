import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TblWorkingHoursDetailComponent } from './tbl-working-hours-detail.component';

describe('TblWorkingHours Management Detail Component', () => {
  let comp: TblWorkingHoursDetailComponent;
  let fixture: ComponentFixture<TblWorkingHoursDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TblWorkingHoursDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tblWorkingHours: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TblWorkingHoursDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TblWorkingHoursDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tblWorkingHours on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tblWorkingHours).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
