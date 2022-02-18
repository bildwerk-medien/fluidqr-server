import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../shared/shared.module';
import { GOOGLE_LOGIN_ROUTE } from './google-login.route';
import { GoogleLoginComponent } from './google-login.component';

@NgModule({
  imports: [SharedModule, RouterModule.forRoot([GOOGLE_LOGIN_ROUTE], { useHash: true })],
  declarations: [GoogleLoginComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class GoogleLoginModule {}
