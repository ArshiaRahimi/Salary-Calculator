import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TblEmployeeInformationDetailComponent } from './tbl-employee-information-detail.component';

describe('TblEmployeeInformation Management Detail Component', () => {
  let comp: TblEmployeeInformationDetailComponent;
  let fixture: ComponentFixture<TblEmployeeInformationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TblEmployeeInformationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tblEmployeeInformation: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TblEmployeeInformationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TblEmployeeInformationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tblEmployeeInformation on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tblEmployeeInformation).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
