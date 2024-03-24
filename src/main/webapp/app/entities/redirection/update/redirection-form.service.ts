import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRedirection, NewRedirection } from '../redirection.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRedirection for edit and NewRedirectionFormGroupInput for create.
 */
type RedirectionFormGroupInput = IRedirection | PartialWithRequiredKeyOf<NewRedirection>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRedirection | NewRedirection> = Omit<T, 'creation' | 'startDate' | 'endDate'> & {
  creation?: string | null;
  startDate?: string | null;
  endDate?: string | null;
};

type RedirectionFormRawValue = FormValueOf<IRedirection>;

type NewRedirectionFormRawValue = FormValueOf<NewRedirection>;

type RedirectionFormDefaults = Pick<NewRedirection, 'id' | 'enabled' | 'creation' | 'startDate' | 'endDate'>;

type RedirectionFormGroupContent = {
  id: FormControl<RedirectionFormRawValue['id'] | NewRedirection['id']>;
  description: FormControl<RedirectionFormRawValue['description']>;
  code: FormControl<RedirectionFormRawValue['code']>;
  url: FormControl<RedirectionFormRawValue['url']>;
  enabled: FormControl<RedirectionFormRawValue['enabled']>;
  creation: FormControl<RedirectionFormRawValue['creation']>;
  startDate: FormControl<RedirectionFormRawValue['startDate']>;
  endDate: FormControl<RedirectionFormRawValue['endDate']>;
  user: FormControl<RedirectionFormRawValue['user']>;
  qrCode: FormControl<RedirectionFormRawValue['qrCode']>;
};

export type RedirectionFormGroup = FormGroup<RedirectionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RedirectionFormService {
  createRedirectionFormGroup(redirection: RedirectionFormGroupInput = { id: null }): RedirectionFormGroup {
    const redirectionRawValue = this.convertRedirectionToRedirectionRawValue({
      ...this.getFormDefaults(),
      ...redirection,
    });
    return new FormGroup<RedirectionFormGroupContent>({
      id: new FormControl(
        { value: redirectionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      description: new FormControl(redirectionRawValue.description),
      code: new FormControl(redirectionRawValue.code),
      url: new FormControl(redirectionRawValue.url, {
        validators: [Validators.required],
      }),
      enabled: new FormControl(redirectionRawValue.enabled, {
        validators: [Validators.required],
      }),
      creation: new FormControl(redirectionRawValue.creation),
      startDate: new FormControl(redirectionRawValue.startDate),
      endDate: new FormControl(redirectionRawValue.endDate),
      user: new FormControl(redirectionRawValue.user),
      qrCode: new FormControl(redirectionRawValue.qrCode),
    });
  }

  getRedirection(form: RedirectionFormGroup): IRedirection | NewRedirection {
    return this.convertRedirectionRawValueToRedirection(form.getRawValue() as RedirectionFormRawValue | NewRedirectionFormRawValue);
  }

  resetForm(form: RedirectionFormGroup, redirection: RedirectionFormGroupInput): void {
    const redirectionRawValue = this.convertRedirectionToRedirectionRawValue({ ...this.getFormDefaults(), ...redirection });
    form.reset(
      {
        ...redirectionRawValue,
        id: { value: redirectionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RedirectionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      enabled: false,
      creation: currentTime,
      startDate: currentTime,
      endDate: currentTime,
    };
  }

  private convertRedirectionRawValueToRedirection(
    rawRedirection: RedirectionFormRawValue | NewRedirectionFormRawValue,
  ): IRedirection | NewRedirection {
    return {
      ...rawRedirection,
      creation: dayjs(rawRedirection.creation, DATE_TIME_FORMAT),
      startDate: dayjs(rawRedirection.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawRedirection.endDate, DATE_TIME_FORMAT),
    };
  }

  private convertRedirectionToRedirectionRawValue(
    redirection: IRedirection | (Partial<NewRedirection> & RedirectionFormDefaults),
  ): RedirectionFormRawValue | PartialWithRequiredKeyOf<NewRedirectionFormRawValue> {
    return {
      ...redirection,
      creation: redirection.creation ? redirection.creation.format(DATE_TIME_FORMAT) : undefined,
      startDate: redirection.startDate ? redirection.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: redirection.endDate ? redirection.endDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
