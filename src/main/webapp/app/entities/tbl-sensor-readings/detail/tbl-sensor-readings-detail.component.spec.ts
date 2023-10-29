import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TblSensorReadingsDetailComponent } from './tbl-sensor-readings-detail.component';

describe('TblSensorReadings Management Detail Component', () => {
  let comp: TblSensorReadingsDetailComponent;
  let fixture: ComponentFixture<TblSensorReadingsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TblSensorReadingsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tblSensorReadings: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TblSensorReadingsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TblSensorReadingsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tblSensorReadings on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tblSensorReadings).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
