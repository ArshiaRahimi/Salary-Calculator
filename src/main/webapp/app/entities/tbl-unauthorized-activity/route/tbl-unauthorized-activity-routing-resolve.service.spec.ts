import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ITblUnauthorizedActivity } from '../tbl-unauthorized-activity.model';
import { TblUnauthorizedActivityService } from '../service/tbl-unauthorized-activity.service';

import { TblUnauthorizedActivityRoutingResolveService } from './tbl-unauthorized-activity-routing-resolve.service';

describe('TblUnauthorizedActivity routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TblUnauthorizedActivityRoutingResolveService;
  let service: TblUnauthorizedActivityService;
  let resultTblUnauthorizedActivity: ITblUnauthorizedActivity | null | undefined;

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
    routingResolveService = TestBed.inject(TblUnauthorizedActivityRoutingResolveService);
    service = TestBed.inject(TblUnauthorizedActivityService);
    resultTblUnauthorizedActivity = undefined;
  });

  describe('resolve', () => {
    it('should return ITblUnauthorizedActivity returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTblUnauthorizedActivity = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTblUnauthorizedActivity).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTblUnauthorizedActivity = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTblUnauthorizedActivity).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ITblUnauthorizedActivity>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTblUnauthorizedActivity = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTblUnauthorizedActivity).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
