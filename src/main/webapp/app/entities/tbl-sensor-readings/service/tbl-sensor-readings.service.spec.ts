import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITblSensorReadings } from '../tbl-sensor-readings.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../tbl-sensor-readings.test-samples';

import { TblSensorReadingsService, RestTblSensorReadings } from './tbl-sensor-readings.service';

const requireRestSample: RestTblSensorReadings = {
  ...sampleWithRequiredData,
  readingTime: sampleWithRequiredData.readingTime?.toJSON(),
};

describe('TblSensorReadings Service', () => {
  let service: TblSensorReadingsService;
  let httpMock: HttpTestingController;
  let expectedResult: ITblSensorReadings | ITblSensorReadings[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TblSensorReadingsService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a TblSensorReadings', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const tblSensorReadings = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tblSensorReadings).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TblSensorReadings', () => {
      const tblSensorReadings = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tblSensorReadings).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TblSensorReadings', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TblSensorReadings', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TblSensorReadings', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTblSensorReadingsToCollectionIfMissing', () => {
      it('should add a TblSensorReadings to an empty array', () => {
        const tblSensorReadings: ITblSensorReadings = sampleWithRequiredData;
        expectedResult = service.addTblSensorReadingsToCollectionIfMissing([], tblSensorReadings);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tblSensorReadings);
      });

      it('should not add a TblSensorReadings to an array that contains it', () => {
        const tblSensorReadings: ITblSensorReadings = sampleWithRequiredData;
        const tblSensorReadingsCollection: ITblSensorReadings[] = [
          {
            ...tblSensorReadings,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTblSensorReadingsToCollectionIfMissing(tblSensorReadingsCollection, tblSensorReadings);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TblSensorReadings to an array that doesn't contain it", () => {
        const tblSensorReadings: ITblSensorReadings = sampleWithRequiredData;
        const tblSensorReadingsCollection: ITblSensorReadings[] = [sampleWithPartialData];
        expectedResult = service.addTblSensorReadingsToCollectionIfMissing(tblSensorReadingsCollection, tblSensorReadings);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tblSensorReadings);
      });

      it('should add only unique TblSensorReadings to an array', () => {
        const tblSensorReadingsArray: ITblSensorReadings[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tblSensorReadingsCollection: ITblSensorReadings[] = [sampleWithRequiredData];
        expectedResult = service.addTblSensorReadingsToCollectionIfMissing(tblSensorReadingsCollection, ...tblSensorReadingsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tblSensorReadings: ITblSensorReadings = sampleWithRequiredData;
        const tblSensorReadings2: ITblSensorReadings = sampleWithPartialData;
        expectedResult = service.addTblSensorReadingsToCollectionIfMissing([], tblSensorReadings, tblSensorReadings2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tblSensorReadings);
        expect(expectedResult).toContain(tblSensorReadings2);
      });

      it('should accept null and undefined values', () => {
        const tblSensorReadings: ITblSensorReadings = sampleWithRequiredData;
        expectedResult = service.addTblSensorReadingsToCollectionIfMissing([], null, tblSensorReadings, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tblSensorReadings);
      });

      it('should return initial array if no TblSensorReadings is added', () => {
        const tblSensorReadingsCollection: ITblSensorReadings[] = [sampleWithRequiredData];
        expectedResult = service.addTblSensorReadingsToCollectionIfMissing(tblSensorReadingsCollection, undefined, null);
        expect(expectedResult).toEqual(tblSensorReadingsCollection);
      });
    });

    describe('compareTblSensorReadings', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTblSensorReadings(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTblSensorReadings(entity1, entity2);
        const compareResult2 = service.compareTblSensorReadings(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTblSensorReadings(entity1, entity2);
        const compareResult2 = service.compareTblSensorReadings(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTblSensorReadings(entity1, entity2);
        const compareResult2 = service.compareTblSensorReadings(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
