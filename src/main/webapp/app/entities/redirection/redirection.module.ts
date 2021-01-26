import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FluidQrServerSharedModule } from 'app/shared/shared.module';
import { RedirectionComponent } from './redirection.component';
import { RedirectionDetailComponent } from './redirection-detail.component';
import { RedirectionUpdateComponent } from './redirection-update.component';
import { RedirectionDeleteDialogComponent } from './redirection-delete-dialog.component';
import { redirectionRoute } from './redirection.route';

@NgModule({
  imports: [FluidQrServerSharedModule, RouterModule.forChild(redirectionRoute)],
  declarations: [RedirectionComponent, RedirectionDetailComponent, RedirectionUpdateComponent, RedirectionDeleteDialogComponent],
  entryComponents: [RedirectionDeleteDialogComponent],
})
export class FluidQrServerRedirectionModule {}
