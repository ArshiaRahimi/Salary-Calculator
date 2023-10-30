import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITblOffDay } from '../tbl-off-day.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../tbl-off-day.test-samples';

import { TblOffDayService } from './tbl-off-day.service';

const requireRestSample: ITblOffDay = {
  ...sampleWithRequiredData,
};

describe('TblOffDay Service', () => {
  let service: TblOffDayService;
  let httpMock: HttpTestingController;
  let expectedResult: ITblOffDay | ITblOffDay[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TblOffDayService);
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

    it('should create a TblOffDay', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const tblOffDay = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tblOffDay).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TblOffDay', () => {
      const tblOffDay = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tblOffDay).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TblOffDay', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TblOffDay', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TblOffDay', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTblOffDayToCollectionIfMissing', () => {
      it('should add a TblOffDay to an empty array', () => {
        const tblOffDay: ITblOffDay = sampleWithRequiredData;
        expectedResult = service.addTblOffDayToCollectionIfMissing([], tblOffDay);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tblOffDay);
      });

      it('should not add a TblOffDay to an array that contains it', () => {
        const tblOffDay: ITblOffDay = sampleWithRequiredData;
        const tblOffDayCollection: ITblOffDay[] = [
          {
            ...tblOffDay,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTblOffDayToCollectionIfMissing(tblOffDayCollection, tblOffDay);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TblOffDay to an array that doesn't contain it", () => {
        const tblOffDay: ITblOffDay = sampleWithRequiredData;
        const tblOffDayCollection: ITblOffDay[] = [sampleWithPartialData];
        expectedResult = service.addTblOffDayToCollectionIfMissing(tblOffDayCollection, tblOffDay);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tblOffDay);
      });

      it('should add only unique TblOffDay to an array', () => {
        const tblOffDayArray: ITblOffDay[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tblOffDayCollection: ITblOffDay[] = [sampleWithRequiredData];
        expectedResult = service.addTblOffDayToCollectionIfMissing(tblOffDayCollection, ...tblOffDayArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tblOffDay: ITblOffDay = sampleWithRequiredData;
        const tblOffDay2: ITblOffDay = sampleWithPartialData;
        expectedResult = service.addTblOffDayToCollectionIfMissing([], tblOffDay, tblOffDay2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tblOffDay);
        expect(expectedResult).toContain(tblOffDay2);
      });

      it('should accept null and undefined values', () => {
        const tblOffDay: ITblOffDay = sampleWithRequiredData;
        expectedResult = service.addTblOffDayToCollectionIfMissing([], null, tblOffDay, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tblOffDay);
      });

      it('should return initial array if no TblOffDay is added', () => {
        const tblOffDayCollection: ITblOffDay[] = [sampleWithRequiredData];
        expectedResult = service.addTblOffDayToCollectionIfMissing(tblOffDayCollection, undefined, null);
        expect(expectedResult).toEqual(tblOffDayCollection);
      });
    });

    describe('compareTblOffDay', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTblOffDay(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTblOffDay(entity1, entity2);
        const compareResult2 = service.compareTblOffDay(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTblOffDay(entity1, entity2);
        const compareResult2 = service.compareTblOffDay(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTblOffDay(entity1, entity2);
        const compareResult2 = service.compareTblOffDay(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
