import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IRedirection, Redirection } from 'app/shared/model/redirection.model';
import { RedirectionService } from './redirection.service';
import { IQrCode } from 'app/shared/model/qr-code.model';
import { QrCodeService } from 'app/entities/qr-code/qr-code.service';

@Component({
  selector: 'jhi-redirection-update',
  templateUrl: './redirection-update.component.html',
})
export class RedirectionUpdateComponent implements OnInit {
  isSaving = false;
  qrcodes: IQrCode[] = [];

  editForm = this.fb.group({
    id: [],
    description: [],
    code: [],
    url: [null, [Validators.required]],
    enabled: [null, [Validators.required]],
    creation: [],
    startDate: [],
    endDate: [],
    qrCode: [],
  });

  constructor(
    protected redirectionService: RedirectionService,
    protected qrCodeService: QrCodeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ redirection }) => {
      if (!redirection.id) {
        const today = moment().startOf('day');
        redirection.creation = today;
        redirection.startDate = today;
        redirection.endDate = today;
      }

      this.updateForm(redirection);

      this.qrCodeService.query().subscribe((res: HttpResponse<IQrCode[]>) => (this.qrcodes = res.body || []));
    });
  }

  updateForm(redirection: IRedirection): void {
    this.editForm.patchValue({
      id: redirection.id,
      description: redirection.description,
      code: redirection.code,
      url: redirection.url,
      enabled: redirection.enabled,
      creation: redirection.creation ? redirection.creation.format(DATE_TIME_FORMAT) : null,
      startDate: redirection.startDate ? redirection.startDate.format(DATE_TIME_FORMAT) : null,
      endDate: redirection.endDate ? redirection.endDate.format(DATE_TIME_FORMAT) : null,
      qrCode: redirection.qrCode,
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

  private createFromForm(): IRedirection {
    return {
      ...new Redirection(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      code: this.editForm.get(['code'])!.value,
      url: this.editForm.get(['url'])!.value,
      enabled: this.editForm.get(['enabled'])!.value,
      creation: this.editForm.get(['creation'])!.value ? moment(this.editForm.get(['creation'])!.value, DATE_TIME_FORMAT) : undefined,
      startDate: this.editForm.get(['startDate'])!.value ? moment(this.editForm.get(['startDate'])!.value, DATE_TIME_FORMAT) : undefined,
      endDate: this.editForm.get(['endDate'])!.value ? moment(this.editForm.get(['endDate'])!.value, DATE_TIME_FORMAT) : undefined,
      qrCode: this.editForm.get(['qrCode'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRedirection>>): void {
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

  trackById(index: number, item: IQrCode): any {
    return item.id;
  }
}
