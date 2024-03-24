import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IQrCode } from '../qr-code.model';
import { QrCodeService } from '../service/qr-code.service';
import { QrCodeFormService, QrCodeFormGroup } from './qr-code-form.service';

@Component({
  standalone: true,
  selector: 'jhi-qr-code-update',
  templateUrl: './qr-code-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class QrCodeUpdateComponent implements OnInit {
  isSaving = false;
  qrCode: IQrCode | null = null;

  usersSharedCollection: IUser[] = [];

  editForm: QrCodeFormGroup = this.qrCodeFormService.createQrCodeFormGroup();

  constructor(
    protected qrCodeService: QrCodeService,
    protected qrCodeFormService: QrCodeFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ qrCode }) => {
      this.qrCode = qrCode;
      if (qrCode) {
        this.updateForm(qrCode);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const qrCode = this.qrCodeFormService.getQrCode(this.editForm);
    if (qrCode.id !== null) {
      this.subscribeToSaveResponse(this.qrCodeService.update(qrCode));
    } else {
      this.subscribeToSaveResponse(this.qrCodeService.create(qrCode));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQrCode>>): void {
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

  protected updateForm(qrCode: IQrCode): void {
    this.qrCode = qrCode;
    this.qrCodeFormService.resetForm(this.editForm, qrCode);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, qrCode.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.qrCode?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
