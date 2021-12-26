import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IQrCode, QrCode } from 'app/shared/model/qr-code.model';
import { QrCodeService } from './qr-code.service';
import { QrCodeComponent } from './qr-code.component';
import { QrCodeDetailComponent } from './qr-code-detail.component';
import { QrCodeUpdateComponent } from './qr-code-update.component';

@Injectable({ providedIn: 'root' })
export class QrCodeResolve implements Resolve<IQrCode> {
  constructor(private service: QrCodeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IQrCode> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((qrCode: HttpResponse<QrCode>) => {
          if (qrCode.body) {
            return of(qrCode.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new QrCode());
  }
}

export const qrCodeRoute: Routes = [
  {
    path: '',
    component: QrCodeComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fluidQrServerApp.qrCode.home.brand',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QrCodeDetailComponent,
    resolve: {
      qrCode: QrCodeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fluidQrServerApp.qrCode.home.brand',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QrCodeUpdateComponent,
    resolve: {
      qrCode: QrCodeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fluidQrServerApp.qrCode.home.brand',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QrCodeUpdateComponent,
    resolve: {
      qrCode: QrCodeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fluidQrServerApp.qrCode.home.brand',
    },
    canActivate: [UserRouteAccessService],
  },
];
