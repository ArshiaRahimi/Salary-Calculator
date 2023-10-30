import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TblUnauthorizedActivityFormService } from './tbl-unauthorized-activity-form.service';
import { TblUnauthorizedActivityService } from '../service/tbl-unauthorized-activity.service';
import { ITblUnauthorizedActivity } from '../tbl-unauthorized-activity.model';

import { TblUnauthorizedActivityUpdateComponent } from './tbl-unauthorized-activity-update.component';

describe('TblUnauthorizedActivity Management Update Component', () => {
  let comp: TblUnauthorizedActivityUpdateComponent;
  let fixture: ComponentFixture<TblUnauthorizedActivityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tblUnauthorizedActivityFormService: TblUnauthorizedActivityFormService;
  let tblUnauthorizedActivityService: TblUnauthorizedActivityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TblUnauthorizedActivityUpdateComponent],
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
      .overrideTemplate(TblUnauthorizedActivityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TblUnauthorizedActivityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tblUnauthorizedActivityFormService = TestBed.inject(TblUnauthorizedActivityFormService);
    tblUnauthorizedActivityService = TestBed.inject(TblUnauthorizedActivityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tblUnauthorizedActivity: ITblUnauthorizedActivity = { id: 456 };

      activatedRoute.data = of({ tblUnauthorizedActivity });
      comp.ngOnInit();

      expect(comp.tblUnauthorizedActivity).toEqual(tblUnauthorizedActivity);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITblUnauthorizedActivity>>();
      const tblUnauthorizedActivity = { id: 123 };
      jest.spyOn(tblUnauthorizedActivityFormService, 'getTblUnauthorizedActivity').mockReturnValue(tblUnauthorizedActivity);
      jest.spyOn(tblUnauthorizedActivityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tblUnauthorizedActivity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tblUnauthorizedActivity }));
      saveSubject.complete();

      // THEN
      expect(tblUnauthorizedActivityFormService.getTblUnauthorizedActivity).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tblUnauthorizedActivityService.update).toHaveBeenCalledWith(expect.objectContaining(tblUnauthorizedActivity));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITblUnauthorizedActivity>>();
      const tblUnauthorizedActivity = { id: 123 };
      jest.spyOn(tblUnauthorizedActivityFormService, 'getTblUnauthorizedActivity').mockReturnValue({ id: null });
      jest.spyOn(tblUnauthorizedActivityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tblUnauthorizedActivity: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tblUnauthorizedActivity }));
      saveSubject.complete();

      // THEN
      expect(tblUnauthorizedActivityFormService.getTblUnauthorizedActivity).toHaveBeenCalled();
      expect(tblUnauthorizedActivityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITblUnauthorizedActivity>>();
      const tblUnauthorizedActivity = { id: 123 };
      jest.spyOn(tblUnauthorizedActivityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tblUnauthorizedActivity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tblUnauthorizedActivityService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
