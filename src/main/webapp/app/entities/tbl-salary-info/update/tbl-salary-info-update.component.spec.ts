import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TblSalaryInfoFormService } from './tbl-salary-info-form.service';
import { TblSalaryInfoService } from '../service/tbl-salary-info.service';
import { ITblSalaryInfo } from '../tbl-salary-info.model';

import { TblSalaryInfoUpdateComponent } from './tbl-salary-info-update.component';

describe('TblSalaryInfo Management Update Component', () => {
  let comp: TblSalaryInfoUpdateComponent;
  let fixture: ComponentFixture<TblSalaryInfoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tblSalaryInfoFormService: TblSalaryInfoFormService;
  let tblSalaryInfoService: TblSalaryInfoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TblSalaryInfoUpdateComponent],
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
      .overrideTemplate(TblSalaryInfoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TblSalaryInfoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tblSalaryInfoFormService = TestBed.inject(TblSalaryInfoFormService);
    tblSalaryInfoService = TestBed.inject(TblSalaryInfoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tblSalaryInfo: ITblSalaryInfo = { id: 456 };

      activatedRoute.data = of({ tblSalaryInfo });
      comp.ngOnInit();

      expect(comp.tblSalaryInfo).toEqual(tblSalaryInfo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITblSalaryInfo>>();
      const tblSalaryInfo = { id: 123 };
      jest.spyOn(tblSalaryInfoFormService, 'getTblSalaryInfo').mockReturnValue(tblSalaryInfo);
      jest.spyOn(tblSalaryInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tblSalaryInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tblSalaryInfo }));
      saveSubject.complete();

      // THEN
      expect(tblSalaryInfoFormService.getTblSalaryInfo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tblSalaryInfoService.update).toHaveBeenCalledWith(expect.objectContaining(tblSalaryInfo));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITblSalaryInfo>>();
      const tblSalaryInfo = { id: 123 };
      jest.spyOn(tblSalaryInfoFormService, 'getTblSalaryInfo').mockReturnValue({ id: null });
      jest.spyOn(tblSalaryInfoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tblSalaryInfo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tblSalaryInfo }));
      saveSubject.complete();

      // THEN
      expect(tblSalaryInfoFormService.getTblSalaryInfo).toHaveBeenCalled();
      expect(tblSalaryInfoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITblSalaryInfo>>();
      const tblSalaryInfo = { id: 123 };
      jest.spyOn(tblSalaryInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tblSalaryInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tblSalaryInfoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
