import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';

import { IGoogleUser } from '../google-user.model';
import { GoogleUserService } from '../service/google-user.service';

@Component({
  selector: 'jhi-google-user',
  templateUrl: './google-user.component.html',
})
export class GoogleUserComponent implements OnInit {
  googleUsers?: IGoogleUser[];
  isLoading = false;

  constructor(protected googleUserService: GoogleUserService) {}

  loadAll(): void {
    this.isLoading = true;

    this.googleUserService.query().subscribe({
      next: (res: HttpResponse<IGoogleUser[]>) => {
        this.isLoading = false;
        this.googleUsers = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IGoogleUser): number {
    return item.id!;
  }
}
