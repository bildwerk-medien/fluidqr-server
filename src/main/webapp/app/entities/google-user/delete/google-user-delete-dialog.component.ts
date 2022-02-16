import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGoogleUser } from '../google-user.model';
import { GoogleUserService } from '../service/google-user.service';

@Component({
  templateUrl: './google-user-delete-dialog.component.html',
})
export class GoogleUserDeleteDialogComponent {
  googleUser?: IGoogleUser;

  constructor(protected googleUserService: GoogleUserService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.googleUserService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
