import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRedirection } from '../redirection.model';
import { RedirectionService } from '../service/redirection.service';

@Component({
  standalone: true,
  templateUrl: './redirection-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RedirectionDeleteDialogComponent {
  redirection?: IRedirection;

  constructor(
    protected redirectionService: RedirectionService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.redirectionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
