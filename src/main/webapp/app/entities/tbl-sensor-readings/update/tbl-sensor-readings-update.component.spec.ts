import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TblSensorReadingsFormService } from './tbl-sensor-readings-form.service';
import { TblSensorReadingsService } from '../service/tbl-sensor-readings.service';
import { ITblSensorReadings } from '../tbl-sensor-readings.model';

import { TblSensorReadingsUpdateComponent } from './tbl-sensor-readings-update.component';

describe('TblSensorReadings Management Update Component', () => {
  let comp: TblSensorReadingsUpdateComponent;
  let fixture: ComponentFixture<TblSensorReadingsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tblSensorReadingsFormService: TblSensorReadingsFormService;
  let tblSensorReadingsService: TblSensorReadingsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TblSensorReadingsUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TblSensorReadingsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TblSensorReadingsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tblSensorReadingsFormService = TestBed.inject(TblSensorReadingsFormService);
    tblSensorReadingsService = TestBed.inject(TblSensorReadingsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tblSensorReadings: ITblSensorReadings = { id: 456 };

      activatedRoute.data = of({ tblSensorReadings });
      comp.ngOnInit();

      expect(comp.tblSensorReadings).toEqual(tblSensorReadings);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITblSensorReadings>>();
      const tblSensorReadings = { id: 123 };
      jest.spyOn(tblSensorReadingsFormService, 'getTblSensorReadings').mockReturnValue(tblSensorReadings);
      jest.spyOn(tblSensorReadingsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tblSensorReadings });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tblSensorReadings }));
      saveSubject.complete();

      // THEN
      expect(tblSensorReadingsFormService.getTblSensorReadings).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tblSensorReadingsService.update).toHaveBeenCalledWith(expect.objectContaining(tblSensorReadings));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITblSensorReadings>>();
      const tblSensorReadings = { id: 123 };
      jest.spyOn(tblSensorReadingsFormService, 'getTblSensorReadings').mockReturnValue({ id: null });
      jest.spyOn(tblSensorReadingsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tblSensorReadings: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tblSensorReadings }));
      saveSubject.complete();

      // THEN
      expect(tblSensorReadingsFormService.getTblSensorReadings).toHaveBeenCalled();
      expect(tblSensorReadingsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITblSensorReadings>>();
      const tblSensorReadings = { id: 123 };
      jest.spyOn(tblSensorReadingsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tblSensorReadings });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tblSensorReadingsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
