import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITblEmployeeInformation } from '../tbl-employee-information.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../tbl-employee-information.test-samples';

import { TblEmployeeInformationService } from './tbl-employee-information.service';

const requireRestSample: ITblEmployeeInformation = {
  ...sampleWithRequiredData,
};

describe('TblEmployeeInformation Service', () => {
  let service: TblEmployeeInformationService;
  let httpMock: HttpTestingController;
  let expectedResult: ITblEmployeeInformation | ITblEmployeeInformation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TblEmployeeInformationService);
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

    it('should create a TblEmployeeInformation', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const tblEmployeeInformation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tblEmployeeInformation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TblEmployeeInformation', () => {
      const tblEmployeeInformation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tblEmployeeInformation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TblEmployeeInformation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TblEmployeeInformation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TblEmployeeInformation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTblEmployeeInformationToCollectionIfMissing', () => {
      it('should add a TblEmployeeInformation to an empty array', () => {
        const tblEmployeeInformation: ITblEmployeeInformation = sampleWithRequiredData;
        expectedResult = service.addTblEmployeeInformationToCollectionIfMissing([], tblEmployeeInformation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tblEmployeeInformation);
      });

      it('should not add a TblEmployeeInformation to an array that contains it', () => {
        const tblEmployeeInformation: ITblEmployeeInformation = sampleWithRequiredData;
        const tblEmployeeInformationCollection: ITblEmployeeInformation[] = [
          {
            ...tblEmployeeInformation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTblEmployeeInformationToCollectionIfMissing(tblEmployeeInformationCollection, tblEmployeeInformation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TblEmployeeInformation to an array that doesn't contain it", () => {
        const tblEmployeeInformation: ITblEmployeeInformation = sampleWithRequiredData;
        const tblEmployeeInformationCollection: ITblEmployeeInformation[] = [sampleWithPartialData];
        expectedResult = service.addTblEmployeeInformationToCollectionIfMissing(tblEmployeeInformationCollection, tblEmployeeInformation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tblEmployeeInformation);
      });

      it('should add only unique TblEmployeeInformation to an array', () => {
        const tblEmployeeInformationArray: ITblEmployeeInformation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tblEmployeeInformationCollection: ITblEmployeeInformation[] = [sampleWithRequiredData];
        expectedResult = service.addTblEmployeeInformationToCollectionIfMissing(
          tblEmployeeInformationCollection,
          ...tblEmployeeInformationArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tblEmployeeInformation: ITblEmployeeInformation = sampleWithRequiredData;
        const tblEmployeeInformation2: ITblEmployeeInformation = sampleWithPartialData;
        expectedResult = service.addTblEmployeeInformationToCollectionIfMissing([], tblEmployeeInformation, tblEmployeeInformation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tblEmployeeInformation);
        expect(expectedResult).toContain(tblEmployeeInformation2);
      });

      it('should accept null and undefined values', () => {
        const tblEmployeeInformation: ITblEmployeeInformation = sampleWithRequiredData;
        expectedResult = service.addTblEmployeeInformationToCollectionIfMissing([], null, tblEmployeeInformation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tblEmployeeInformation);
      });

      it('should return initial array if no TblEmployeeInformation is added', () => {
        const tblEmployeeInformationCollection: ITblEmployeeInformation[] = [sampleWithRequiredData];
        expectedResult = service.addTblEmployeeInformationToCollectionIfMissing(tblEmployeeInformationCollection, undefined, null);
        expect(expectedResult).toEqual(tblEmployeeInformationCollection);
      });
    });

    describe('compareTblEmployeeInformation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTblEmployeeInformation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTblEmployeeInformation(entity1, entity2);
        const compareResult2 = service.compareTblEmployeeInformation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTblEmployeeInformation(entity1, entity2);
        const compareResult2 = service.compareTblEmployeeInformation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTblEmployeeInformation(entity1, entity2);
        const compareResult2 = service.compareTblEmployeeInformation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
