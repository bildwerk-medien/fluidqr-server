import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRedirection } from '../redirection.model';
import { RedirectionService } from '../service/redirection.service';

@Component({
  templateUrl: './redirection-delete-dialog.component.html',
})
export class RedirectionDeleteDialogComponent {
  redirection?: IRedirection;

  constructor(protected redirectionService: RedirectionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.redirectionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
