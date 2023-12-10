import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IQrCode, NewQrCode } from '../qr-code.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQrCode for edit and NewQrCodeFormGroupInput for create.
 */
type QrCodeFormGroupInput = IQrCode | PartialWithRequiredKeyOf<NewQrCode>;

type QrCodeFormDefaults = Pick<NewQrCode, 'id'>;

type QrCodeFormGroupContent = {
  id: FormControl<IQrCode['id'] | NewQrCode['id']>;
  code: FormControl<IQrCode['code']>;
  user: FormControl<IQrCode['user']>;
};

export type QrCodeFormGroup = FormGroup<QrCodeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QrCodeFormService {
  createQrCodeFormGroup(qrCode: QrCodeFormGroupInput = { id: null }): QrCodeFormGroup {
    const qrCodeRawValue = {
      ...this.getFormDefaults(),
      ...qrCode,
    };
    return new FormGroup<QrCodeFormGroupContent>({
      id: new FormControl(
        { value: qrCodeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(qrCodeRawValue.code, {
        validators: [Validators.required],
      }),
      user: new FormControl(qrCodeRawValue.user),
    });
  }

  getQrCode(form: QrCodeFormGroup): IQrCode | NewQrCode {
    return form.getRawValue() as IQrCode | NewQrCode;
  }

  resetForm(form: QrCodeFormGroup, qrCode: QrCodeFormGroupInput): void {
    const qrCodeRawValue = { ...this.getFormDefaults(), ...qrCode };
    form.reset(
      {
        ...qrCodeRawValue,
        id: { value: qrCodeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): QrCodeFormDefaults {
    return {
      id: null,
    };
  }
}
