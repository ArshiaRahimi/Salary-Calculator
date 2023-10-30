import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ITblWorkingHours } from '../tbl-working-hours.model';
import { TblWorkingHoursService } from '../service/tbl-working-hours.service';

import { TblWorkingHoursRoutingResolveService } from './tbl-working-hours-routing-resolve.service';

describe('TblWorkingHours routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TblWorkingHoursRoutingResolveService;
  let service: TblWorkingHoursService;
  let resultTblWorkingHours: ITblWorkingHours | null | undefined;

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
    routingResolveService = TestBed.inject(TblWorkingHoursRoutingResolveService);
    service = TestBed.inject(TblWorkingHoursService);
    resultTblWorkingHours = undefined;
  });

  describe('resolve', () => {
    it('should return ITblWorkingHours returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTblWorkingHours = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTblWorkingHours).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTblWorkingHours = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTblWorkingHours).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ITblWorkingHours>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTblWorkingHours = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTblWorkingHours).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
