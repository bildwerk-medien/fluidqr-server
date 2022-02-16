import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GoogleUserComponent } from '../list/google-user.component';
import { GoogleUserDetailComponent } from '../detail/google-user-detail.component';
import { GoogleUserUpdateComponent } from '../update/google-user-update.component';
import { GoogleUserRoutingResolveService } from './google-user-routing-resolve.service';

const googleUserRoute: Routes = [
  {
    path: '',
    component: GoogleUserComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GoogleUserDetailComponent,
    resolve: {
      googleUser: GoogleUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GoogleUserUpdateComponent,
    resolve: {
      googleUser: GoogleUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GoogleUserUpdateComponent,
    resolve: {
      googleUser: GoogleUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(googleUserRoute)],
  exports: [RouterModule],
})
export class GoogleUserRoutingModule {}
