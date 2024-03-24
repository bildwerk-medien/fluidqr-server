import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../qr-code.test-samples';

import { QrCodeFormService } from './qr-code-form.service';

describe('QrCode Form Service', () => {
  let service: QrCodeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QrCodeFormService);
  });

  describe('Service methods', () => {
    describe('createQrCodeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createQrCodeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });

      it('passing IQrCode should create a new form with FormGroup', () => {
        const formGroup = service.createQrCodeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });
    });

    describe('getQrCode', () => {
      it('should return NewQrCode for default QrCode initial value', () => {
        const formGroup = service.createQrCodeFormGroup(sampleWithNewData);

        const qrCode = service.getQrCode(formGroup) as any;

        expect(qrCode).toMatchObject(sampleWithNewData);
      });

      it('should return NewQrCode for empty QrCode initial value', () => {
        const formGroup = service.createQrCodeFormGroup();

        const qrCode = service.getQrCode(formGroup) as any;

        expect(qrCode).toMatchObject({});
      });

      it('should return IQrCode', () => {
        const formGroup = service.createQrCodeFormGroup(sampleWithRequiredData);

        const qrCode = service.getQrCode(formGroup) as any;

        expect(qrCode).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IQrCode should not enable id FormControl', () => {
        const formGroup = service.createQrCodeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewQrCode should disable id FormControl', () => {
        const formGroup = service.createQrCodeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
