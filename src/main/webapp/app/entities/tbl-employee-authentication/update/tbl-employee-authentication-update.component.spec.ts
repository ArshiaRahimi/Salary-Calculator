import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TblEmployeeAuthenticationFormService } from './tbl-employee-authentication-form.service';
import { TblEmployeeAuthenticationService } from '../service/tbl-employee-authentication.service';
import { ITblEmployeeAuthentication } from '../tbl-employee-authentication.model';

import { TblEmployeeAuthenticationUpdateComponent } from './tbl-employee-authentication-update.component';

describe('TblEmployeeAuthentication Management Update Component', () => {
  let comp: TblEmployeeAuthenticationUpdateComponent;
  let fixture: ComponentFixture<TblEmployeeAuthenticationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tblEmployeeAuthenticationFormService: TblEmployeeAuthenticationFormService;
  let tblEmployeeAuthenticationService: TblEmployeeAuthenticationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TblEmployeeAuthenticationUpdateComponent],
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
      .overrideTemplate(TblEmployeeAuthenticationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TblEmployeeAuthenticationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tblEmployeeAuthenticationFormService = TestBed.inject(TblEmployeeAuthenticationFormService);
    tblEmployeeAuthenticationService = TestBed.inject(TblEmployeeAuthenticationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tblEmployeeAuthentication: ITblEmployeeAuthentication = { id: 456 };

      activatedRoute.data = of({ tblEmployeeAuthentication });
      comp.ngOnInit();

      expect(comp.tblEmployeeAuthentication).toEqual(tblEmployeeAuthentication);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITblEmployeeAuthentication>>();
      const tblEmployeeAuthentication = { id: 123 };
      jest.spyOn(tblEmployeeAuthenticationFormService, 'getTblEmployeeAuthentication').mockReturnValue(tblEmployeeAuthentication);
      jest.spyOn(tblEmployeeAuthenticationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tblEmployeeAuthentication });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tblEmployeeAuthentication }));
      saveSubject.complete();

      // THEN
      expect(tblEmployeeAuthenticationFormService.getTblEmployeeAuthentication).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tblEmployeeAuthenticationService.update).toHaveBeenCalledWith(expect.objectContaining(tblEmployeeAuthentication));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITblEmployeeAuthentication>>();
      const tblEmployeeAuthentication = { id: 123 };
      jest.spyOn(tblEmployeeAuthenticationFormService, 'getTblEmployeeAuthentication').mockReturnValue({ id: null });
      jest.spyOn(tblEmployeeAuthenticationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tblEmployeeAuthentication: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tblEmployeeAuthentication }));
      saveSubject.complete();

      // THEN
      expect(tblEmployeeAuthenticationFormService.getTblEmployeeAuthentication).toHaveBeenCalled();
      expect(tblEmployeeAuthenticationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITblEmployeeAuthentication>>();
      const tblEmployeeAuthentication = { id: 123 };
      jest.spyOn(tblEmployeeAuthenticationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tblEmployeeAuthentication });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tblEmployeeAuthenticationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
