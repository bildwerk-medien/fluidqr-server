import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRedirection } from '../redirection.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../redirection.test-samples';

import { RedirectionService, RestRedirection } from './redirection.service';

const requireRestSample: RestRedirection = {
  ...sampleWithRequiredData,
  creation: sampleWithRequiredData.creation?.toJSON(),
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.toJSON(),
};

describe('Redirection Service', () => {
  let service: RedirectionService;
  let httpMock: HttpTestingController;
  let expectedResult: IRedirection | IRedirection[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RedirectionService);
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

    it('should create a Redirection', () => {
      const redirection = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(redirection).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Redirection', () => {
      const redirection = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(redirection).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Redirection', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Redirection', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Redirection', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRedirectionToCollectionIfMissing', () => {
      it('should add a Redirection to an empty array', () => {
        const redirection: IRedirection = sampleWithRequiredData;
        expectedResult = service.addRedirectionToCollectionIfMissing([], redirection);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(redirection);
      });

      it('should not add a Redirection to an array that contains it', () => {
        const redirection: IRedirection = sampleWithRequiredData;
        const redirectionCollection: IRedirection[] = [
          {
            ...redirection,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRedirectionToCollectionIfMissing(redirectionCollection, redirection);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Redirection to an array that doesn't contain it", () => {
        const redirection: IRedirection = sampleWithRequiredData;
        const redirectionCollection: IRedirection[] = [sampleWithPartialData];
        expectedResult = service.addRedirectionToCollectionIfMissing(redirectionCollection, redirection);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(redirection);
      });

      it('should add only unique Redirection to an array', () => {
        const redirectionArray: IRedirection[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const redirectionCollection: IRedirection[] = [sampleWithRequiredData];
        expectedResult = service.addRedirectionToCollectionIfMissing(redirectionCollection, ...redirectionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const redirection: IRedirection = sampleWithRequiredData;
        const redirection2: IRedirection = sampleWithPartialData;
        expectedResult = service.addRedirectionToCollectionIfMissing([], redirection, redirection2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(redirection);
        expect(expectedResult).toContain(redirection2);
      });

      it('should accept null and undefined values', () => {
        const redirection: IRedirection = sampleWithRequiredData;
        expectedResult = service.addRedirectionToCollectionIfMissing([], null, redirection, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(redirection);
      });

      it('should return initial array if no Redirection is added', () => {
        const redirectionCollection: IRedirection[] = [sampleWithRequiredData];
        expectedResult = service.addRedirectionToCollectionIfMissing(redirectionCollection, undefined, null);
        expect(expectedResult).toEqual(redirectionCollection);
      });
    });

    describe('compareRedirection', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRedirection(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRedirection(entity1, entity2);
        const compareResult2 = service.compareRedirection(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRedirection(entity1, entity2);
        const compareResult2 = service.compareRedirection(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRedirection(entity1, entity2);
        const compareResult2 = service.compareRedirection(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
