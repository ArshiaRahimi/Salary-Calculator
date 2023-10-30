import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITblWorkingHours } from '../tbl-working-hours.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../tbl-working-hours.test-samples';

import { TblWorkingHoursService } from './tbl-working-hours.service';

const requireRestSample: ITblWorkingHours = {
  ...sampleWithRequiredData,
};

describe('TblWorkingHours Service', () => {
  let service: TblWorkingHoursService;
  let httpMock: HttpTestingController;
  let expectedResult: ITblWorkingHours | ITblWorkingHours[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TblWorkingHoursService);
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

    it('should create a TblWorkingHours', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const tblWorkingHours = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tblWorkingHours).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TblWorkingHours', () => {
      const tblWorkingHours = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tblWorkingHours).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TblWorkingHours', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TblWorkingHours', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TblWorkingHours', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTblWorkingHoursToCollectionIfMissing', () => {
      it('should add a TblWorkingHours to an empty array', () => {
        const tblWorkingHours: ITblWorkingHours = sampleWithRequiredData;
        expectedResult = service.addTblWorkingHoursToCollectionIfMissing([], tblWorkingHours);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tblWorkingHours);
      });

      it('should not add a TblWorkingHours to an array that contains it', () => {
        const tblWorkingHours: ITblWorkingHours = sampleWithRequiredData;
        const tblWorkingHoursCollection: ITblWorkingHours[] = [
          {
            ...tblWorkingHours,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTblWorkingHoursToCollectionIfMissing(tblWorkingHoursCollection, tblWorkingHours);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TblWorkingHours to an array that doesn't contain it", () => {
        const tblWorkingHours: ITblWorkingHours = sampleWithRequiredData;
        const tblWorkingHoursCollection: ITblWorkingHours[] = [sampleWithPartialData];
        expectedResult = service.addTblWorkingHoursToCollectionIfMissing(tblWorkingHoursCollection, tblWorkingHours);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tblWorkingHours);
      });

      it('should add only unique TblWorkingHours to an array', () => {
        const tblWorkingHoursArray: ITblWorkingHours[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tblWorkingHoursCollection: ITblWorkingHours[] = [sampleWithRequiredData];
        expectedResult = service.addTblWorkingHoursToCollectionIfMissing(tblWorkingHoursCollection, ...tblWorkingHoursArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tblWorkingHours: ITblWorkingHours = sampleWithRequiredData;
        const tblWorkingHours2: ITblWorkingHours = sampleWithPartialData;
        expectedResult = service.addTblWorkingHoursToCollectionIfMissing([], tblWorkingHours, tblWorkingHours2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tblWorkingHours);
        expect(expectedResult).toContain(tblWorkingHours2);
      });

      it('should accept null and undefined values', () => {
        const tblWorkingHours: ITblWorkingHours = sampleWithRequiredData;
        expectedResult = service.addTblWorkingHoursToCollectionIfMissing([], null, tblWorkingHours, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tblWorkingHours);
      });

      it('should return initial array if no TblWorkingHours is added', () => {
        const tblWorkingHoursCollection: ITblWorkingHours[] = [sampleWithRequiredData];
        expectedResult = service.addTblWorkingHoursToCollectionIfMissing(tblWorkingHoursCollection, undefined, null);
        expect(expectedResult).toEqual(tblWorkingHoursCollection);
      });
    });

    describe('compareTblWorkingHours', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTblWorkingHours(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTblWorkingHours(entity1, entity2);
        const compareResult2 = service.compareTblWorkingHours(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTblWorkingHours(entity1, entity2);
        const compareResult2 = service.compareTblWorkingHours(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTblWorkingHours(entity1, entity2);
        const compareResult2 = service.compareTblWorkingHours(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
