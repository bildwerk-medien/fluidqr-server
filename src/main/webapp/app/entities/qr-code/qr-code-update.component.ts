import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IQrCode, QrCode } from 'app/shared/model/qr-code.model';
import { QrCodeService } from './qr-code.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-qr-code-update',
  templateUrl: './qr-code-update.component.html',
})
export class QrCodeUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    code: [null, [Validators.required]],
    user: [],
  });

  constructor(
    protected qrCodeService: QrCodeService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ qrCode }) => {
      this.updateForm(qrCode);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(qrCode: IQrCode): void {
    this.editForm.patchValue({
      id: qrCode.id,
      code: qrCode.code,
      user: qrCode.user,
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

  private createFromForm(): IQrCode {
    return {
      ...new QrCode(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQrCode>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
