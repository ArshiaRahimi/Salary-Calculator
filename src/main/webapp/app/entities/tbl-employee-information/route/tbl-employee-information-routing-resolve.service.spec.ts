import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ITblEmployeeInformation } from '../tbl-employee-information.model';
import { TblEmployeeInformationService } from '../service/tbl-employee-information.service';

import { TblEmployeeInformationRoutingResolveService } from './tbl-employee-information-routing-resolve.service';

describe('TblEmployeeInformation routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TblEmployeeInformationRoutingResolveService;
  let service: TblEmployeeInformationService;
  let resultTblEmployeeInformation: ITblEmployeeInformation | null | undefined;

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
    routingResolveService = TestBed.inject(TblEmployeeInformationRoutingResolveService);
    service = TestBed.inject(TblEmployeeInformationService);
    resultTblEmployeeInformation = undefined;
  });

  describe('resolve', () => {
    it('should return ITblEmployeeInformation returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTblEmployeeInformation = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTblEmployeeInformation).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTblEmployeeInformation = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTblEmployeeInformation).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ITblEmployeeInformation>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTblEmployeeInformation = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTblEmployeeInformation).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
