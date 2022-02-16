import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGoogleUser } from '../google-user.model';
import { GoogleUserService } from '../service/google-user.service';
import { GoogleUserDeleteDialogComponent } from '../delete/google-user-delete-dialog.component';

@Component({
  selector: 'jhi-google-user',
  templateUrl: './google-user.component.html',
})
export class GoogleUserComponent implements OnInit {
  googleUsers?: IGoogleUser[];
  isLoading = false;

  constructor(protected googleUserService: GoogleUserService, protected modalService: NgbModal) {}

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

  delete(googleUser: IGoogleUser): void {
    const modalRef = this.modalService.open(GoogleUserDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.googleUser = googleUser;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
