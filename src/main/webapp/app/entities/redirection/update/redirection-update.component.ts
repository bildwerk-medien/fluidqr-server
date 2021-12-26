import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IRedirection, Redirection } from '../redirection.model';
import { RedirectionService } from '../service/redirection.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IQrCode } from 'app/entities/qr-code/qr-code.model';
import { QrCodeService } from 'app/entities/qr-code/service/qr-code.service';

@Component({
  selector: 'jhi-redirection-update',
  templateUrl: './redirection-update.component.html',
})
export class RedirectionUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  qrCodesSharedCollection: IQrCode[] = [];

  editForm = this.fb.group({
    id: [],
    description: [],
    code: [],
    url: [null, [Validators.required]],
    enabled: [null, [Validators.required]],
    creation: [],
    startDate: [],
    endDate: [],
    user: [],
    qrCode: [],
  });

  constructor(
    protected redirectionService: RedirectionService,
    protected userService: UserService,
    protected qrCodeService: QrCodeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ redirection }) => {
      if (redirection.id === undefined) {
        const today = dayjs().startOf('day');
        redirection.creation = today;
        redirection.startDate = today;
        redirection.endDate = today;
      }

      this.updateForm(redirection);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const redirection = this.createFromForm();
    if (redirection.id !== undefined) {
      this.subscribeToSaveResponse(this.redirectionService.update(redirection));
    } else {
      this.subscribeToSaveResponse(this.redirectionService.create(redirection));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackQrCodeById(index: number, item: IQrCode): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRedirection>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(redirection: IRedirection): void {
    this.editForm.patchValue({
      id: redirection.id,
      description: redirection.description,
      code: redirection.code,
      url: redirection.url,
      enabled: redirection.enabled,
      creation: redirection.creation ? redirection.creation.format(DATE_TIME_FORMAT) : null,
      startDate: redirection.startDate ? redirection.startDate.format(DATE_TIME_FORMAT) : null,
      endDate: redirection.endDate ? redirection.endDate.format(DATE_TIME_FORMAT) : null,
      user: redirection.user,
      qrCode: redirection.qrCode,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, redirection.user);
    this.qrCodesSharedCollection = this.qrCodeService.addQrCodeToCollectionIfMissing(this.qrCodesSharedCollection, redirection.qrCode);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.qrCodeService
      .query()
      .pipe(map((res: HttpResponse<IQrCode[]>) => res.body ?? []))
      .pipe(map((qrCodes: IQrCode[]) => this.qrCodeService.addQrCodeToCollectionIfMissing(qrCodes, this.editForm.get('qrCode')!.value)))
      .subscribe((qrCodes: IQrCode[]) => (this.qrCodesSharedCollection = qrCodes));
  }

  protected createFromForm(): IRedirection {
    return {
      ...new Redirection(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      code: this.editForm.get(['code'])!.value,
      url: this.editForm.get(['url'])!.value,
      enabled: this.editForm.get(['enabled'])!.value,
      creation: this.editForm.get(['creation'])!.value ? dayjs(this.editForm.get(['creation'])!.value, DATE_TIME_FORMAT) : undefined,
      startDate: this.editForm.get(['startDate'])!.value ? dayjs(this.editForm.get(['startDate'])!.value, DATE_TIME_FORMAT) : undefined,
      endDate: this.editForm.get(['endDate'])!.value ? dayjs(this.editForm.get(['endDate'])!.value, DATE_TIME_FORMAT) : undefined,
      user: this.editForm.get(['user'])!.value,
      qrCode: this.editForm.get(['qrCode'])!.value,
    };
  }
}
