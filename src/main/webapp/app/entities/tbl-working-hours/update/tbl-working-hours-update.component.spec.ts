import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TblWorkingHoursFormService } from './tbl-working-hours-form.service';
import { TblWorkingHoursService } from '../service/tbl-working-hours.service';
import { ITblWorkingHours } from '../tbl-working-hours.model';

import { TblWorkingHoursUpdateComponent } from './tbl-working-hours-update.component';

describe('TblWorkingHours Management Update Component', () => {
  let comp: TblWorkingHoursUpdateComponent;
  let fixture: ComponentFixture<TblWorkingHoursUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tblWorkingHoursFormService: TblWorkingHoursFormService;
  let tblWorkingHoursService: TblWorkingHoursService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TblWorkingHoursUpdateComponent],
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
      .overrideTemplate(TblWorkingHoursUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TblWorkingHoursUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tblWorkingHoursFormService = TestBed.inject(TblWorkingHoursFormService);
    tblWorkingHoursService = TestBed.inject(TblWorkingHoursService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tblWorkingHours: ITblWorkingHours = { id: 456 };

      activatedRoute.data = of({ tblWorkingHours });
      comp.ngOnInit();

      expect(comp.tblWorkingHours).toEqual(tblWorkingHours);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITblWorkingHours>>();
      const tblWorkingHours = { id: 123 };
      jest.spyOn(tblWorkingHoursFormService, 'getTblWorkingHours').mockReturnValue(tblWorkingHours);
      jest.spyOn(tblWorkingHoursService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tblWorkingHours });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tblWorkingHours }));
      saveSubject.complete();

      // THEN
      expect(tblWorkingHoursFormService.getTblWorkingHours).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tblWorkingHoursService.update).toHaveBeenCalledWith(expect.objectContaining(tblWorkingHours));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITblWorkingHours>>();
      const tblWorkingHours = { id: 123 };
      jest.spyOn(tblWorkingHoursFormService, 'getTblWorkingHours').mockReturnValue({ id: null });
      jest.spyOn(tblWorkingHoursService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tblWorkingHours: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tblWorkingHours }));
      saveSubject.complete();

      // THEN
      expect(tblWorkingHoursFormService.getTblWorkingHours).toHaveBeenCalled();
      expect(tblWorkingHoursService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITblWorkingHours>>();
      const tblWorkingHours = { id: 123 };
      jest.spyOn(tblWorkingHoursService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tblWorkingHours });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tblWorkingHoursService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
