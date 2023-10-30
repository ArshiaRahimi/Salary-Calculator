import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ITblEmployeeAuthentication } from '../tbl-employee-authentication.model';
import { TblEmployeeAuthenticationService } from '../service/tbl-employee-authentication.service';

import { TblEmployeeAuthenticationRoutingResolveService } from './tbl-employee-authentication-routing-resolve.service';

describe('TblEmployeeAuthentication routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TblEmployeeAuthenticationRoutingResolveService;
  let service: TblEmployeeAuthenticationService;
  let resultTblEmployeeAuthentication: ITblEmployeeAuthentication | null | undefined;

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
    routingResolveService = TestBed.inject(TblEmployeeAuthenticationRoutingResolveService);
    service = TestBed.inject(TblEmployeeAuthenticationService);
    resultTblEmployeeAuthentication = undefined;
  });

  describe('resolve', () => {
    it('should return ITblEmployeeAuthentication returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTblEmployeeAuthentication = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTblEmployeeAuthentication).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTblEmployeeAuthentication = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTblEmployeeAuthentication).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ITblEmployeeAuthentication>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTblEmployeeAuthentication = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTblEmployeeAuthentication).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
