import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITblSalaryInfo } from '../tbl-salary-info.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../tbl-salary-info.test-samples';

import { TblSalaryInfoService } from './tbl-salary-info.service';

const requireRestSample: ITblSalaryInfo = {
  ...sampleWithRequiredData,
};

describe('TblSalaryInfo Service', () => {
  let service: TblSalaryInfoService;
  let httpMock: HttpTestingController;
  let expectedResult: ITblSalaryInfo | ITblSalaryInfo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TblSalaryInfoService);
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

    it('should create a TblSalaryInfo', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const tblSalaryInfo = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tblSalaryInfo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TblSalaryInfo', () => {
      const tblSalaryInfo = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tblSalaryInfo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TblSalaryInfo', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TblSalaryInfo', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TblSalaryInfo', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTblSalaryInfoToCollectionIfMissing', () => {
      it('should add a TblSalaryInfo to an empty array', () => {
        const tblSalaryInfo: ITblSalaryInfo = sampleWithRequiredData;
        expectedResult = service.addTblSalaryInfoToCollectionIfMissing([], tblSalaryInfo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tblSalaryInfo);
      });

      it('should not add a TblSalaryInfo to an array that contains it', () => {
        const tblSalaryInfo: ITblSalaryInfo = sampleWithRequiredData;
        const tblSalaryInfoCollection: ITblSalaryInfo[] = [
          {
            ...tblSalaryInfo,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTblSalaryInfoToCollectionIfMissing(tblSalaryInfoCollection, tblSalaryInfo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TblSalaryInfo to an array that doesn't contain it", () => {
        const tblSalaryInfo: ITblSalaryInfo = sampleWithRequiredData;
        const tblSalaryInfoCollection: ITblSalaryInfo[] = [sampleWithPartialData];
        expectedResult = service.addTblSalaryInfoToCollectionIfMissing(tblSalaryInfoCollection, tblSalaryInfo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tblSalaryInfo);
      });

      it('should add only unique TblSalaryInfo to an array', () => {
        const tblSalaryInfoArray: ITblSalaryInfo[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tblSalaryInfoCollection: ITblSalaryInfo[] = [sampleWithRequiredData];
        expectedResult = service.addTblSalaryInfoToCollectionIfMissing(tblSalaryInfoCollection, ...tblSalaryInfoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tblSalaryInfo: ITblSalaryInfo = sampleWithRequiredData;
        const tblSalaryInfo2: ITblSalaryInfo = sampleWithPartialData;
        expectedResult = service.addTblSalaryInfoToCollectionIfMissing([], tblSalaryInfo, tblSalaryInfo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tblSalaryInfo);
        expect(expectedResult).toContain(tblSalaryInfo2);
      });

      it('should accept null and undefined values', () => {
        const tblSalaryInfo: ITblSalaryInfo = sampleWithRequiredData;
        expectedResult = service.addTblSalaryInfoToCollectionIfMissing([], null, tblSalaryInfo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tblSalaryInfo);
      });

      it('should return initial array if no TblSalaryInfo is added', () => {
        const tblSalaryInfoCollection: ITblSalaryInfo[] = [sampleWithRequiredData];
        expectedResult = service.addTblSalaryInfoToCollectionIfMissing(tblSalaryInfoCollection, undefined, null);
        expect(expectedResult).toEqual(tblSalaryInfoCollection);
      });
    });

    describe('compareTblSalaryInfo', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTblSalaryInfo(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTblSalaryInfo(entity1, entity2);
        const compareResult2 = service.compareTblSalaryInfo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTblSalaryInfo(entity1, entity2);
        const compareResult2 = service.compareTblSalaryInfo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTblSalaryInfo(entity1, entity2);
        const compareResult2 = service.compareTblSalaryInfo(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
