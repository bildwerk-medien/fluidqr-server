import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RedirectionComponent } from './list/redirection.component';
import { RedirectionDetailComponent } from './detail/redirection-detail.component';
import { RedirectionUpdateComponent } from './update/redirection-update.component';
import { RedirectionDeleteDialogComponent } from './delete/redirection-delete-dialog.component';
import { RedirectionRoutingModule } from './route/redirection-routing.module';

@NgModule({
  imports: [SharedModule, RedirectionRoutingModule],
  declarations: [RedirectionComponent, RedirectionDetailComponent, RedirectionUpdateComponent, RedirectionDeleteDialogComponent],
  entryComponents: [RedirectionDeleteDialogComponent],
})
export class RedirectionModule {}
