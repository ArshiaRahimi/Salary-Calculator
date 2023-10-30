import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TblEmployeeInformationFormService } from './tbl-employee-information-form.service';
import { TblEmployeeInformationService } from '../service/tbl-employee-information.service';
import { ITblEmployeeInformation } from '../tbl-employee-information.model';

import { TblEmployeeInformationUpdateComponent } from './tbl-employee-information-update.component';

describe('TblEmployeeInformation Management Update Component', () => {
  let comp: TblEmployeeInformationUpdateComponent;
  let fixture: ComponentFixture<TblEmployeeInformationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tblEmployeeInformationFormService: TblEmployeeInformationFormService;
  let tblEmployeeInformationService: TblEmployeeInformationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TblEmployeeInformationUpdateComponent],
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
      .overrideTemplate(TblEmployeeInformationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TblEmployeeInformationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tblEmployeeInformationFormService = TestBed.inject(TblEmployeeInformationFormService);
    tblEmployeeInformationService = TestBed.inject(TblEmployeeInformationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tblEmployeeInformation: ITblEmployeeInformation = { id: 456 };

      activatedRoute.data = of({ tblEmployeeInformation });
      comp.ngOnInit();

      expect(comp.tblEmployeeInformation).toEqual(tblEmployeeInformation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITblEmployeeInformation>>();
      const tblEmployeeInformation = { id: 123 };
      jest.spyOn(tblEmployeeInformationFormService, 'getTblEmployeeInformation').mockReturnValue(tblEmployeeInformation);
      jest.spyOn(tblEmployeeInformationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tblEmployeeInformation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tblEmployeeInformation }));
      saveSubject.complete();

      // THEN
      expect(tblEmployeeInformationFormService.getTblEmployeeInformation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tblEmployeeInformationService.update).toHaveBeenCalledWith(expect.objectContaining(tblEmployeeInformation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITblEmployeeInformation>>();
      const tblEmployeeInformation = { id: 123 };
      jest.spyOn(tblEmployeeInformationFormService, 'getTblEmployeeInformation').mockReturnValue({ id: null });
      jest.spyOn(tblEmployeeInformationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tblEmployeeInformation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tblEmployeeInformation }));
      saveSubject.complete();

      // THEN
      expect(tblEmployeeInformationFormService.getTblEmployeeInformation).toHaveBeenCalled();
      expect(tblEmployeeInformationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITblEmployeeInformation>>();
      const tblEmployeeInformation = { id: 123 };
      jest.spyOn(tblEmployeeInformationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tblEmployeeInformation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tblEmployeeInformationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
