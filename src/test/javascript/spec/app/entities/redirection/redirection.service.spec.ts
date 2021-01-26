import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { RedirectionService } from 'app/entities/redirection/redirection.service';
import { IRedirection, Redirection } from 'app/shared/model/redirection.model';

describe('Service Tests', () => {
  describe('Redirection Service', () => {
    let injector: TestBed;
    let service: RedirectionService;
    let httpMock: HttpTestingController;
    let elemDefault: IRedirection;
    let expectedResult: IRedirection | IRedirection[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(RedirectionService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Redirection(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', false, currentDate, currentDate, currentDate);
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

      it('should return a list of Redirection', () => {
        const returnedFromService = Object.assign(
          {
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
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
