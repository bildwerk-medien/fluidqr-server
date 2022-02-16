import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IGoogleUser, GoogleUser } from '../google-user.model';

import { GoogleUserService } from './google-user.service';

describe('GoogleUser Service', () => {
  let service: GoogleUserService;
  let httpMock: HttpTestingController;
  let elemDefault: IGoogleUser;
  let expectedResult: IGoogleUser | IGoogleUser[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GoogleUserService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      refreshToken: 'AAAAAAA',
      enabled: false,
      creationTime: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          creationTime: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a GoogleUser', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          creationTime: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          creationTime: currentDate,
        },
        returnedFromService
      );

      service.create(new GoogleUser()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GoogleUser', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          refreshToken: 'BBBBBB',
          enabled: true,
          creationTime: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          creationTime: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a GoogleUser', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new GoogleUser()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          creationTime: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GoogleUser', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          refreshToken: 'BBBBBB',
          enabled: true,
          creationTime: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          creationTime: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a GoogleUser', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addGoogleUserToCollectionIfMissing', () => {
      it('should add a GoogleUser to an empty array', () => {
        const googleUser: IGoogleUser = { id: 123 };
        expectedResult = service.addGoogleUserToCollectionIfMissing([], googleUser);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(googleUser);
      });

      it('should not add a GoogleUser to an array that contains it', () => {
        const googleUser: IGoogleUser = { id: 123 };
        const googleUserCollection: IGoogleUser[] = [
          {
            ...googleUser,
          },
          { id: 456 },
        ];
        expectedResult = service.addGoogleUserToCollectionIfMissing(googleUserCollection, googleUser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GoogleUser to an array that doesn't contain it", () => {
        const googleUser: IGoogleUser = { id: 123 };
        const googleUserCollection: IGoogleUser[] = [{ id: 456 }];
        expectedResult = service.addGoogleUserToCollectionIfMissing(googleUserCollection, googleUser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(googleUser);
      });

      it('should add only unique GoogleUser to an array', () => {
        const googleUserArray: IGoogleUser[] = [{ id: 123 }, { id: 456 }, { id: 53871 }];
        const googleUserCollection: IGoogleUser[] = [{ id: 123 }];
        expectedResult = service.addGoogleUserToCollectionIfMissing(googleUserCollection, ...googleUserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const googleUser: IGoogleUser = { id: 123 };
        const googleUser2: IGoogleUser = { id: 456 };
        expectedResult = service.addGoogleUserToCollectionIfMissing([], googleUser, googleUser2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(googleUser);
        expect(expectedResult).toContain(googleUser2);
      });

      it('should accept null and undefined values', () => {
        const googleUser: IGoogleUser = { id: 123 };
        expectedResult = service.addGoogleUserToCollectionIfMissing([], null, googleUser, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(googleUser);
      });

      it('should return initial array if no GoogleUser is added', () => {
        const googleUserCollection: IGoogleUser[] = [{ id: 123 }];
        expectedResult = service.addGoogleUserToCollectionIfMissing(googleUserCollection, undefined, null);
        expect(expectedResult).toEqual(googleUserCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
