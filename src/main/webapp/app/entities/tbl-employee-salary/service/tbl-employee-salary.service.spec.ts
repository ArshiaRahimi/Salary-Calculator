import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITblEmployeeSalary } from '../tbl-employee-salary.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../tbl-employee-salary.test-samples';

import { TblEmployeeSalaryService, RestTblEmployeeSalary } from './tbl-employee-salary.service';

const requireRestSample: RestTblEmployeeSalary = {
  ...sampleWithRequiredData,
  dateCalculated: sampleWithRequiredData.dateCalculated?.toJSON(),
};

describe('TblEmployeeSalary Service', () => {
  let service: TblEmployeeSalaryService;
  let httpMock: HttpTestingController;
  let expectedResult: ITblEmployeeSalary | ITblEmployeeSalary[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TblEmployeeSalaryService);
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

    it('should create a TblEmployeeSalary', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const tblEmployeeSalary = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tblEmployeeSalary).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TblEmployeeSalary', () => {
      const tblEmployeeSalary = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tblEmployeeSalary).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TblEmployeeSalary', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TblEmployeeSalary', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TblEmployeeSalary', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTblEmployeeSalaryToCollectionIfMissing', () => {
      it('should add a TblEmployeeSalary to an empty array', () => {
        const tblEmployeeSalary: ITblEmployeeSalary = sampleWithRequiredData;
        expectedResult = service.addTblEmployeeSalaryToCollectionIfMissing([], tblEmployeeSalary);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tblEmployeeSalary);
      });

      it('should not add a TblEmployeeSalary to an array that contains it', () => {
        const tblEmployeeSalary: ITblEmployeeSalary = sampleWithRequiredData;
        const tblEmployeeSalaryCollection: ITblEmployeeSalary[] = [
          {
            ...tblEmployeeSalary,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTblEmployeeSalaryToCollectionIfMissing(tblEmployeeSalaryCollection, tblEmployeeSalary);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TblEmployeeSalary to an array that doesn't contain it", () => {
        const tblEmployeeSalary: ITblEmployeeSalary = sampleWithRequiredData;
        const tblEmployeeSalaryCollection: ITblEmployeeSalary[] = [sampleWithPartialData];
        expectedResult = service.addTblEmployeeSalaryToCollectionIfMissing(tblEmployeeSalaryCollection, tblEmployeeSalary);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tblEmployeeSalary);
      });

      it('should add only unique TblEmployeeSalary to an array', () => {
        const tblEmployeeSalaryArray: ITblEmployeeSalary[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tblEmployeeSalaryCollection: ITblEmployeeSalary[] = [sampleWithRequiredData];
        expectedResult = service.addTblEmployeeSalaryToCollectionIfMissing(tblEmployeeSalaryCollection, ...tblEmployeeSalaryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tblEmployeeSalary: ITblEmployeeSalary = sampleWithRequiredData;
        const tblEmployeeSalary2: ITblEmployeeSalary = sampleWithPartialData;
        expectedResult = service.addTblEmployeeSalaryToCollectionIfMissing([], tblEmployeeSalary, tblEmployeeSalary2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tblEmployeeSalary);
        expect(expectedResult).toContain(tblEmployeeSalary2);
      });

      it('should accept null and undefined values', () => {
        const tblEmployeeSalary: ITblEmployeeSalary = sampleWithRequiredData;
        expectedResult = service.addTblEmployeeSalaryToCollectionIfMissing([], null, tblEmployeeSalary, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tblEmployeeSalary);
      });

      it('should return initial array if no TblEmployeeSalary is added', () => {
        const tblEmployeeSalaryCollection: ITblEmployeeSalary[] = [sampleWithRequiredData];
        expectedResult = service.addTblEmployeeSalaryToCollectionIfMissing(tblEmployeeSalaryCollection, undefined, null);
        expect(expectedResult).toEqual(tblEmployeeSalaryCollection);
      });
    });

    describe('compareTblEmployeeSalary', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTblEmployeeSalary(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTblEmployeeSalary(entity1, entity2);
        const compareResult2 = service.compareTblEmployeeSalary(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTblEmployeeSalary(entity1, entity2);
        const compareResult2 = service.compareTblEmployeeSalary(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTblEmployeeSalary(entity1, entity2);
        const compareResult2 = service.compareTblEmployeeSalary(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
