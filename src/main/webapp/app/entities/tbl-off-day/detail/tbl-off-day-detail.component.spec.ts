import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TblOffDayDetailComponent } from './tbl-off-day-detail.component';

describe('TblOffDay Management Detail Component', () => {
  let comp: TblOffDayDetailComponent;
  let fixture: ComponentFixture<TblOffDayDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TblOffDayDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tblOffDay: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TblOffDayDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TblOffDayDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tblOffDay on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tblOffDay).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
