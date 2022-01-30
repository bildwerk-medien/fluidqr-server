import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRedirection, Redirection } from '../redirection.model';

import { RedirectionService } from './redirection.service';

describe('Redirection Service', () => {
  let service: RedirectionService;
  let httpMock: HttpTestingController;
  let elemDefault: IRedirection;
  let expectedResult: IRedirection | IRedirection[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RedirectionService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      description: 'AAAAAAA',
      code: 'AAAAAAA',
      url: 'AAAAAAA',
      enabled: false,
      creation: currentDate,
      startDate: currentDate,
      endDate: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          creation: currentDate.format(DATE_TIME_FORMAT),
          startDate: currentDate.format(DATE_TIME_FORMAT),
          endDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Redirection', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          creation: currentDate.format(DATE_TIME_FORMAT),
          startDate: currentDate.format(DATE_TIME_FORMAT),
          endDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          creation: currentDate,
          startDate: currentDate,
          endDate: currentDate,
        },
        returnedFromService
      );

      service.create(new Redirection()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Redirection', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          code: 'BBBBBB',
          url: 'BBBBBB',
          enabled: true,
          creation: currentDate.format(DATE_TIME_FORMAT),
          startDate: currentDate.format(DATE_TIME_FORMAT),
          endDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          creation: currentDate,
          startDate: currentDate,
          endDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Redirection', () => {
      const patchObject = Object.assign(
        {
          description: 'BBBBBB',
          code: 'BBBBBB',
          url: 'BBBBBB',
          startDate: currentDate.format(DATE_TIME_FORMAT),
          endDate: currentDate.format(DATE_TIME_FORMAT),
        },
        new Redirection()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          creation: currentDate,
          startDate: currentDate,
          endDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Redirection', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          code: 'BBBBBB',
          url: 'BBBBBB',
          enabled: true,
          creation: currentDate.format(DATE_TIME_FORMAT),
          startDate: currentDate.format(DATE_TIME_FORMAT),
          endDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          creation: currentDate,
          startDate: currentDate,
          endDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Redirection', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRedirectionToCollectionIfMissing', () => {
      it('should add a Redirection to an empty array', () => {
        const redirection: IRedirection = { id: 123 };
        expectedResult = service.addRedirectionToCollectionIfMissing([], redirection);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(redirection);
      });

      it('should not add a Redirection to an array that contains it', () => {
        const redirection: IRedirection = { id: 123 };
        const redirectionCollection: IRedirection[] = [
          {
            ...redirection,
          },
          { id: 456 },
        ];
        expectedResult = service.addRedirectionToCollectionIfMissing(redirectionCollection, redirection);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Redirection to an array that doesn't contain it", () => {
        const redirection: IRedirection = { id: 123 };
        const redirectionCollection: IRedirection[] = [{ id: 456 }];
        expectedResult = service.addRedirectionToCollectionIfMissing(redirectionCollection, redirection);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(redirection);
      });

      it('should add only unique Redirection to an array', () => {
        const redirectionArray: IRedirection[] = [{ id: 123 }, { id: 456 }, { id: 93203 }];
        const redirectionCollection: IRedirection[] = [{ id: 123 }];
        expectedResult = service.addRedirectionToCollectionIfMissing(redirectionCollection, ...redirectionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const redirection: IRedirection = { id: 123 };
        const redirection2: IRedirection = { id: 456 };
        expectedResult = service.addRedirectionToCollectionIfMissing([], redirection, redirection2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(redirection);
        expect(expectedResult).toContain(redirection2);
      });

      it('should accept null and undefined values', () => {
        const redirection: IRedirection = { id: 123 };
        expectedResult = service.addRedirectionToCollectionIfMissing([], null, redirection, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(redirection);
      });

      it('should return initial array if no Redirection is added', () => {
        const redirectionCollection: IRedirection[] = [{ id: 123 }];
        expectedResult = service.addRedirectionToCollectionIfMissing(redirectionCollection, undefined, null);
        expect(expectedResult).toEqual(redirectionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
