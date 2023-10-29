import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ITblSensorReadings } from '../tbl-sensor-readings.model';
import { TblSensorReadingsService } from '../service/tbl-sensor-readings.service';

import { TblSensorReadingsRoutingResolveService } from './tbl-sensor-readings-routing-resolve.service';

describe('TblSensorReadings routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TblSensorReadingsRoutingResolveService;
  let service: TblSensorReadingsService;
  let resultTblSensorReadings: ITblSensorReadings | null | undefined;

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
    routingResolveService = TestBed.inject(TblSensorReadingsRoutingResolveService);
    service = TestBed.inject(TblSensorReadingsService);
    resultTblSensorReadings = undefined;
  });

  describe('resolve', () => {
    it('should return ITblSensorReadings returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTblSensorReadings = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTblSensorReadings).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTblSensorReadings = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTblSensorReadings).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ITblSensorReadings>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTblSensorReadings = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTblSensorReadings).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
