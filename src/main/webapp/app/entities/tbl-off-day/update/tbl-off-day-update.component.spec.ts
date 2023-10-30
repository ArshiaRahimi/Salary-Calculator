import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TblOffDayFormService } from './tbl-off-day-form.service';
import { TblOffDayService } from '../service/tbl-off-day.service';
import { ITblOffDay } from '../tbl-off-day.model';

import { TblOffDayUpdateComponent } from './tbl-off-day-update.component';

describe('TblOffDay Management Update Component', () => {
  let comp: TblOffDayUpdateComponent;
  let fixture: ComponentFixture<TblOffDayUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tblOffDayFormService: TblOffDayFormService;
  let tblOffDayService: TblOffDayService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TblOffDayUpdateComponent],
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
      .overrideTemplate(TblOffDayUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TblOffDayUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tblOffDayFormService = TestBed.inject(TblOffDayFormService);
    tblOffDayService = TestBed.inject(TblOffDayService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tblOffDay: ITblOffDay = { id: 456 };

      activatedRoute.data = of({ tblOffDay });
      comp.ngOnInit();

      expect(comp.tblOffDay).toEqual(tblOffDay);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITblOffDay>>();
      const tblOffDay = { id: 123 };
      jest.spyOn(tblOffDayFormService, 'getTblOffDay').mockReturnValue(tblOffDay);
      jest.spyOn(tblOffDayService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tblOffDay });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tblOffDay }));
      saveSubject.complete();

      // THEN
      expect(tblOffDayFormService.getTblOffDay).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tblOffDayService.update).toHaveBeenCalledWith(expect.objectContaining(tblOffDay));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITblOffDay>>();
      const tblOffDay = { id: 123 };
      jest.spyOn(tblOffDayFormService, 'getTblOffDay').mockReturnValue({ id: null });
      jest.spyOn(tblOffDayService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tblOffDay: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tblOffDay }));
      saveSubject.complete();

      // THEN
      expect(tblOffDayFormService.getTblOffDay).toHaveBeenCalled();
      expect(tblOffDayService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITblOffDay>>();
      const tblOffDay = { id: 123 };
      jest.spyOn(tblOffDayService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tblOffDay });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tblOffDayService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
