import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IQrCode, QrCode } from '../qr-code.model';

import { QrCodeService } from './qr-code.service';

describe('QrCode Service', () => {
  let service: QrCodeService;
  let httpMock: HttpTestingController;
  let elemDefault: IQrCode;
  let expectedResult: IQrCode | IQrCode[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(QrCodeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      code: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a QrCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new QrCode()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a QrCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          code: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a QrCode', () => {
      const patchObject = Object.assign({}, new QrCode());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of QrCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          code: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a QrCode', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addQrCodeToCollectionIfMissing', () => {
      it('should add a QrCode to an empty array', () => {
        const qrCode: IQrCode = { id: 123 };
        expectedResult = service.addQrCodeToCollectionIfMissing([], qrCode);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(qrCode);
      });

      it('should not add a QrCode to an array that contains it', () => {
        const qrCode: IQrCode = { id: 123 };
        const qrCodeCollection: IQrCode[] = [
          {
            ...qrCode,
          },
          { id: 456 },
        ];
        expectedResult = service.addQrCodeToCollectionIfMissing(qrCodeCollection, qrCode);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a QrCode to an array that doesn't contain it", () => {
        const qrCode: IQrCode = { id: 123 };
        const qrCodeCollection: IQrCode[] = [{ id: 456 }];
        expectedResult = service.addQrCodeToCollectionIfMissing(qrCodeCollection, qrCode);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(qrCode);
      });

      it('should add only unique QrCode to an array', () => {
        const qrCodeArray: IQrCode[] = [{ id: 123 }, { id: 456 }, { id: 67266 }];
        const qrCodeCollection: IQrCode[] = [{ id: 123 }];
        expectedResult = service.addQrCodeToCollectionIfMissing(qrCodeCollection, ...qrCodeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const qrCode: IQrCode = { id: 123 };
        const qrCode2: IQrCode = { id: 456 };
        expectedResult = service.addQrCodeToCollectionIfMissing([], qrCode, qrCode2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(qrCode);
        expect(expectedResult).toContain(qrCode2);
      });

      it('should accept null and undefined values', () => {
        const qrCode: IQrCode = { id: 123 };
        expectedResult = service.addQrCodeToCollectionIfMissing([], null, qrCode, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(qrCode);
      });

      it('should return initial array if no QrCode is added', () => {
        const qrCodeCollection: IQrCode[] = [{ id: 123 }];
        expectedResult = service.addQrCodeToCollectionIfMissing(qrCodeCollection, undefined, null);
        expect(expectedResult).toEqual(qrCodeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
