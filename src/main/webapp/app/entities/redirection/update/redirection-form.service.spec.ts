import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../redirection.test-samples';

import { RedirectionFormService } from './redirection-form.service';

describe('Redirection Form Service', () => {
  let service: RedirectionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RedirectionFormService);
  });

  describe('Service methods', () => {
    describe('createRedirectionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRedirectionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            code: expect.any(Object),
            url: expect.any(Object),
            enabled: expect.any(Object),
            creation: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            user: expect.any(Object),
            qrCode: expect.any(Object),
          }),
        );
      });

      it('passing IRedirection should create a new form with FormGroup', () => {
        const formGroup = service.createRedirectionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            code: expect.any(Object),
            url: expect.any(Object),
            enabled: expect.any(Object),
            creation: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            user: expect.any(Object),
            qrCode: expect.any(Object),
          }),
        );
      });
    });

    describe('getRedirection', () => {
      it('should return NewRedirection for default Redirection initial value', () => {
        const formGroup = service.createRedirectionFormGroup(sampleWithNewData);

        const redirection = service.getRedirection(formGroup) as any;

        expect(redirection).toMatchObject(sampleWithNewData);
      });

      it('should return NewRedirection for empty Redirection initial value', () => {
        const formGroup = service.createRedirectionFormGroup();

        const redirection = service.getRedirection(formGroup) as any;

        expect(redirection).toMatchObject({});
      });

      it('should return IRedirection', () => {
        const formGroup = service.createRedirectionFormGroup(sampleWithRequiredData);

        const redirection = service.getRedirection(formGroup) as any;

        expect(redirection).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRedirection should not enable id FormControl', () => {
        const formGroup = service.createRedirectionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRedirection should disable id FormControl', () => {
        const formGroup = service.createRedirectionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
