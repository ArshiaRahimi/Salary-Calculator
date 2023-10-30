import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TblEmployeeSalaryFormService } from './tbl-employee-salary-form.service';
import { TblEmployeeSalaryService } from '../service/tbl-employee-salary.service';
import { ITblEmployeeSalary } from '../tbl-employee-salary.model';

import { TblEmployeeSalaryUpdateComponent } from './tbl-employee-salary-update.component';

describe('TblEmployeeSalary Management Update Component', () => {
  let comp: TblEmployeeSalaryUpdateComponent;
  let fixture: ComponentFixture<TblEmployeeSalaryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tblEmployeeSalaryFormService: TblEmployeeSalaryFormService;
  let tblEmployeeSalaryService: TblEmployeeSalaryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TblEmployeeSalaryUpdateComponent],
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
      .overrideTemplate(TblEmployeeSalaryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TblEmployeeSalaryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tblEmployeeSalaryFormService = TestBed.inject(TblEmployeeSalaryFormService);
    tblEmployeeSalaryService = TestBed.inject(TblEmployeeSalaryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tblEmployeeSalary: ITblEmployeeSalary = { id: 456 };

      activatedRoute.data = of({ tblEmployeeSalary });
      comp.ngOnInit();

      expect(comp.tblEmployeeSalary).toEqual(tblEmployeeSalary);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITblEmployeeSalary>>();
      const tblEmployeeSalary = { id: 123 };
      jest.spyOn(tblEmployeeSalaryFormService, 'getTblEmployeeSalary').mockReturnValue(tblEmployeeSalary);
      jest.spyOn(tblEmployeeSalaryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tblEmployeeSalary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tblEmployeeSalary }));
      saveSubject.complete();

      // THEN
      expect(tblEmployeeSalaryFormService.getTblEmployeeSalary).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tblEmployeeSalaryService.update).toHaveBeenCalledWith(expect.objectContaining(tblEmployeeSalary));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITblEmployeeSalary>>();
      const tblEmployeeSalary = { id: 123 };
      jest.spyOn(tblEmployeeSalaryFormService, 'getTblEmployeeSalary').mockReturnValue({ id: null });
      jest.spyOn(tblEmployeeSalaryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tblEmployeeSalary: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tblEmployeeSalary }));
      saveSubject.complete();

      // THEN
      expect(tblEmployeeSalaryFormService.getTblEmployeeSalary).toHaveBeenCalled();
      expect(tblEmployeeSalaryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITblEmployeeSalary>>();
      const tblEmployeeSalary = { id: 123 };
      jest.spyOn(tblEmployeeSalaryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tblEmployeeSalary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tblEmployeeSalaryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
