import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IQrCode } from 'app/entities/qr-code/qr-code.model';
import { QrCodeService } from 'app/entities/qr-code/service/qr-code.service';
import { RedirectionService } from '../service/redirection.service';
import { IRedirection } from '../redirection.model';
import { RedirectionFormService, RedirectionFormGroup } from './redirection-form.service';

@Component({
  standalone: true,
  selector: 'jhi-redirection-update',
  templateUrl: './redirection-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RedirectionUpdateComponent implements OnInit {
  isSaving = false;
  redirection: IRedirection | null = null;

  usersSharedCollection: IUser[] = [];
  qrCodesSharedCollection: IQrCode[] = [];

  editForm: RedirectionFormGroup = this.redirectionFormService.createRedirectionFormGroup();

  constructor(
    protected redirectionService: RedirectionService,
    protected redirectionFormService: RedirectionFormService,
    protected userService: UserService,
    protected qrCodeService: QrCodeService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareQrCode = (o1: IQrCode | null, o2: IQrCode | null): boolean => this.qrCodeService.compareQrCode(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ redirection }) => {
      this.redirection = redirection;
      if (redirection) {
        this.updateForm(redirection);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const redirection = this.redirectionFormService.getRedirection(this.editForm);
    if (redirection.id !== null) {
      this.subscribeToSaveResponse(this.redirectionService.update(redirection));
    } else {
      this.subscribeToSaveResponse(this.redirectionService.create(redirection));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRedirection>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
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
    this.redirection = redirection;
    this.redirectionFormService.resetForm(this.editForm, redirection);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, redirection.user);
    this.qrCodesSharedCollection = this.qrCodeService.addQrCodeToCollectionIfMissing<IQrCode>(
      this.qrCodesSharedCollection,
      redirection.qrCode,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.redirection?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.qrCodeService
      .query()
      .pipe(map((res: HttpResponse<IQrCode[]>) => res.body ?? []))
      .pipe(map((qrCodes: IQrCode[]) => this.qrCodeService.addQrCodeToCollectionIfMissing<IQrCode>(qrCodes, this.redirection?.qrCode)))
      .subscribe((qrCodes: IQrCode[]) => (this.qrCodesSharedCollection = qrCodes));
  }
}
