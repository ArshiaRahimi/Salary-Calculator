import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ITblEmployeeSalary } from '../tbl-employee-salary.model';
import { TblEmployeeSalaryService } from '../service/tbl-employee-salary.service';

import { TblEmployeeSalaryRoutingResolveService } from './tbl-employee-salary-routing-resolve.service';

describe('TblEmployeeSalary routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TblEmployeeSalaryRoutingResolveService;
  let service: TblEmployeeSalaryService;
  let resultTblEmployeeSalary: ITblEmployeeSalary | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(TblEmployeeSalaryRoutingResolveService);
    service = TestBed.inject(TblEmployeeSalaryService);
    resultTblEmployeeSalary = undefined;
  });

  describe('resolve', () => {
    it('should return ITblEmployeeSalary returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTblEmployeeSalary = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTblEmployeeSalary).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTblEmployeeSalary = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTblEmployeeSalary).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ITblEmployeeSalary>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTblEmployeeSalary = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTblEmployeeSalary).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
