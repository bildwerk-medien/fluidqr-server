import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IGoogleUser, GoogleUser } from '../google-user.model';
import { GoogleUserService } from '../service/google-user.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-google-user-update',
  templateUrl: './google-user-update.component.html',
})
export class GoogleUserUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    refreshToken: [],
    enabled: [],
    creationTime: [],
    user: [],
  });

  constructor(
    protected googleUserService: GoogleUserService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ googleUser }) => {
      if (googleUser.id === undefined) {
        const today = dayjs().startOf('day');
        googleUser.creationTime = today;
      }

      this.updateForm(googleUser);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const googleUser = this.createFromForm();
    if (googleUser.id !== undefined) {
      this.subscribeToSaveResponse(this.googleUserService.update(googleUser));
    } else {
      this.subscribeToSaveResponse(this.googleUserService.create(googleUser));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGoogleUser>>): void {
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

  protected updateForm(googleUser: IGoogleUser): void {
    this.editForm.patchValue({
      id: googleUser.id,
      name: googleUser.name,
      refreshToken: googleUser.refreshToken,
      enabled: googleUser.enabled,
      creationTime: googleUser.creationTime ? googleUser.creationTime.format(DATE_TIME_FORMAT) : null,
      user: googleUser.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, googleUser.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IGoogleUser {
    return {
      ...new GoogleUser(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      refreshToken: this.editForm.get(['refreshToken'])!.value,
      enabled: this.editForm.get(['enabled'])!.value,
      creationTime: this.editForm.get(['creationTime'])!.value
        ? dayjs(this.editForm.get(['creationTime'])!.value, DATE_TIME_FORMAT)
        : undefined,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
