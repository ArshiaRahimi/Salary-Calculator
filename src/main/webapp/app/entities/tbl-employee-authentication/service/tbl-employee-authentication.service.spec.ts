import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITblEmployeeAuthentication } from '../tbl-employee-authentication.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../tbl-employee-authentication.test-samples';

import { TblEmployeeAuthenticationService } from './tbl-employee-authentication.service';

const requireRestSample: ITblEmployeeAuthentication = {
  ...sampleWithRequiredData,
};

describe('TblEmployeeAuthentication Service', () => {
  let service: TblEmployeeAuthenticationService;
  let httpMock: HttpTestingController;
  let expectedResult: ITblEmployeeAuthentication | ITblEmployeeAuthentication[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TblEmployeeAuthenticationService);
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

    it('should create a TblEmployeeAuthentication', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const tblEmployeeAuthentication = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tblEmployeeAuthentication).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TblEmployeeAuthentication', () => {
      const tblEmployeeAuthentication = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tblEmployeeAuthentication).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TblEmployeeAuthentication', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TblEmployeeAuthentication', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TblEmployeeAuthentication', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTblEmployeeAuthenticationToCollectionIfMissing', () => {
      it('should add a TblEmployeeAuthentication to an empty array', () => {
        const tblEmployeeAuthentication: ITblEmployeeAuthentication = sampleWithRequiredData;
        expectedResult = service.addTblEmployeeAuthenticationToCollectionIfMissing([], tblEmployeeAuthentication);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tblEmployeeAuthentication);
      });

      it('should not add a TblEmployeeAuthentication to an array that contains it', () => {
        const tblEmployeeAuthentication: ITblEmployeeAuthentication = sampleWithRequiredData;
        const tblEmployeeAuthenticationCollection: ITblEmployeeAuthentication[] = [
          {
            ...tblEmployeeAuthentication,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTblEmployeeAuthenticationToCollectionIfMissing(
          tblEmployeeAuthenticationCollection,
          tblEmployeeAuthentication
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TblEmployeeAuthentication to an array that doesn't contain it", () => {
        const tblEmployeeAuthentication: ITblEmployeeAuthentication = sampleWithRequiredData;
        const tblEmployeeAuthenticationCollection: ITblEmployeeAuthentication[] = [sampleWithPartialData];
        expectedResult = service.addTblEmployeeAuthenticationToCollectionIfMissing(
          tblEmployeeAuthenticationCollection,
          tblEmployeeAuthentication
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tblEmployeeAuthentication);
      });

      it('should add only unique TblEmployeeAuthentication to an array', () => {
        const tblEmployeeAuthenticationArray: ITblEmployeeAuthentication[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const tblEmployeeAuthenticationCollection: ITblEmployeeAuthentication[] = [sampleWithRequiredData];
        expectedResult = service.addTblEmployeeAuthenticationToCollectionIfMissing(
          tblEmployeeAuthenticationCollection,
          ...tblEmployeeAuthenticationArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tblEmployeeAuthentication: ITblEmployeeAuthentication = sampleWithRequiredData;
        const tblEmployeeAuthentication2: ITblEmployeeAuthentication = sampleWithPartialData;
        expectedResult = service.addTblEmployeeAuthenticationToCollectionIfMissing(
          [],
          tblEmployeeAuthentication,
          tblEmployeeAuthentication2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tblEmployeeAuthentication);
        expect(expectedResult).toContain(tblEmployeeAuthentication2);
      });

      it('should accept null and undefined values', () => {
        const tblEmployeeAuthentication: ITblEmployeeAuthentication = sampleWithRequiredData;
        expectedResult = service.addTblEmployeeAuthenticationToCollectionIfMissing([], null, tblEmployeeAuthentication, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tblEmployeeAuthentication);
      });

      it('should return initial array if no TblEmployeeAuthentication is added', () => {
        const tblEmployeeAuthenticationCollection: ITblEmployeeAuthentication[] = [sampleWithRequiredData];
        expectedResult = service.addTblEmployeeAuthenticationToCollectionIfMissing(tblEmployeeAuthenticationCollection, undefined, null);
        expect(expectedResult).toEqual(tblEmployeeAuthenticationCollection);
      });
    });

    describe('compareTblEmployeeAuthentication', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTblEmployeeAuthentication(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTblEmployeeAuthentication(entity1, entity2);
        const compareResult2 = service.compareTblEmployeeAuthentication(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTblEmployeeAuthentication(entity1, entity2);
        const compareResult2 = service.compareTblEmployeeAuthentication(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTblEmployeeAuthentication(entity1, entity2);
        const compareResult2 = service.compareTblEmployeeAuthentication(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
