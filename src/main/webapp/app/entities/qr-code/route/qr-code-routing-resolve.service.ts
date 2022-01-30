import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQrCode, QrCode } from '../qr-code.model';
import { QrCodeService } from '../service/qr-code.service';

@Injectable({ providedIn: 'root' })
export class QrCodeRoutingResolveService implements Resolve<IQrCode> {
  constructor(protected service: QrCodeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IQrCode> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((qrCode: HttpResponse<QrCode>) => {
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
