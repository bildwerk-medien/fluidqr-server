import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GoogleUserComponent } from './list/google-user.component';
import { GoogleUserDetailComponent } from './detail/google-user-detail.component';
import { GoogleUserRoutingModule } from './route/google-user-routing.module';

@NgModule({
  imports: [SharedModule, GoogleUserRoutingModule],
  declarations: [GoogleUserComponent, GoogleUserDetailComponent],
})
export class GoogleUserModule {}
