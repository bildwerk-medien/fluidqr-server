import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IQrCode, QrCode } from '../qr-code.model';
import { QrCodeService } from '../service/qr-code.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-qr-code-update',
  templateUrl: './qr-code-update.component.html',
})
export class QrCodeUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    code: [null, [Validators.required]],
    user: [],
  });

  constructor(
    protected qrCodeService: QrCodeService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ qrCode }) => {
      this.updateForm(qrCode);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const qrCode = this.createFromForm();
    if (qrCode.id !== undefined) {
      this.subscribeToSaveResponse(this.qrCodeService.update(qrCode));
    } else {
      this.subscribeToSaveResponse(this.qrCodeService.create(qrCode));
    }
  }

  trackUserById(_index: number, item: IUser): number {
    return item.id!;
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
    this.editForm.patchValue({
      id: qrCode.id,
      code: qrCode.code,
      user: qrCode.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, qrCode.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IQrCode {
    return {
      ...new QrCode(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
