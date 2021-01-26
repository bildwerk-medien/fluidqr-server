import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRedirection } from 'app/shared/model/redirection.model';
import { RedirectionService } from './redirection.service';

@Component({
  templateUrl: './redirection-delete-dialog.component.html',
})
export class RedirectionDeleteDialogComponent {
  redirection?: IRedirection;

  constructor(
    protected redirectionService: RedirectionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.redirectionService.delete(id).subscribe(() => {
      this.eventManager.broadcast('redirectionListModification');
      this.activeModal.close();
    });
  }
}
