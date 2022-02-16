import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GoogleUserComponent } from './list/google-user.component';
import { GoogleUserDetailComponent } from './detail/google-user-detail.component';
import { GoogleUserUpdateComponent } from './update/google-user-update.component';
import { GoogleUserDeleteDialogComponent } from './delete/google-user-delete-dialog.component';
import { GoogleUserRoutingModule } from './route/google-user-routing.module';

@NgModule({
  imports: [SharedModule, GoogleUserRoutingModule],
  declarations: [GoogleUserComponent, GoogleUserDetailComponent, GoogleUserUpdateComponent, GoogleUserDeleteDialogComponent],
  entryComponents: [GoogleUserDeleteDialogComponent],
})
export class GoogleUserModule {}
