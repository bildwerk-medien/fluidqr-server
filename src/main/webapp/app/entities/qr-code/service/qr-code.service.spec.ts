import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IQrCode } from '../qr-code.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../qr-code.test-samples';

import { QrCodeService } from './qr-code.service';

const requireRestSample: IQrCode = {
  ...sampleWithRequiredData,
};

describe('QrCode Service', () => {
  let service: QrCodeService;
  let httpMock: HttpTestingController;
  let expectedResult: IQrCode | IQrCode[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(QrCodeService);
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

    it('should create a QrCode', () => {
      const qrCode = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(qrCode).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a QrCode', () => {
      const qrCode = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(qrCode).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a QrCode', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of QrCode', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a QrCode', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addQrCodeToCollectionIfMissing', () => {
      it('should add a QrCode to an empty array', () => {
        const qrCode: IQrCode = sampleWithRequiredData;
        expectedResult = service.addQrCodeToCollectionIfMissing([], qrCode);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(qrCode);
      });

      it('should not add a QrCode to an array that contains it', () => {
        const qrCode: IQrCode = sampleWithRequiredData;
        const qrCodeCollection: IQrCode[] = [
          {
            ...qrCode,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addQrCodeToCollectionIfMissing(qrCodeCollection, qrCode);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a QrCode to an array that doesn't contain it", () => {
        const qrCode: IQrCode = sampleWithRequiredData;
        const qrCodeCollection: IQrCode[] = [sampleWithPartialData];
        expectedResult = service.addQrCodeToCollectionIfMissing(qrCodeCollection, qrCode);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(qrCode);
      });

      it('should add only unique QrCode to an array', () => {
        const qrCodeArray: IQrCode[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const qrCodeCollection: IQrCode[] = [sampleWithRequiredData];
        expectedResult = service.addQrCodeToCollectionIfMissing(qrCodeCollection, ...qrCodeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const qrCode: IQrCode = sampleWithRequiredData;
        const qrCode2: IQrCode = sampleWithPartialData;
        expectedResult = service.addQrCodeToCollectionIfMissing([], qrCode, qrCode2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(qrCode);
        expect(expectedResult).toContain(qrCode2);
      });

      it('should accept null and undefined values', () => {
        const qrCode: IQrCode = sampleWithRequiredData;
        expectedResult = service.addQrCodeToCollectionIfMissing([], null, qrCode, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(qrCode);
      });

      it('should return initial array if no QrCode is added', () => {
        const qrCodeCollection: IQrCode[] = [sampleWithRequiredData];
        expectedResult = service.addQrCodeToCollectionIfMissing(qrCodeCollection, undefined, null);
        expect(expectedResult).toEqual(qrCodeCollection);
      });
    });

    describe('compareQrCode', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareQrCode(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareQrCode(entity1, entity2);
        const compareResult2 = service.compareQrCode(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareQrCode(entity1, entity2);
        const compareResult2 = service.compareQrCode(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareQrCode(entity1, entity2);
        const compareResult2 = service.compareQrCode(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
