import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQrCode } from '../qr-code.model';
import { QrCodeService } from '../service/qr-code.service';

export const qrCodeResolve = (route: ActivatedRouteSnapshot): Observable<null | IQrCode> => {
  const id = route.params['id'];
  if (id) {
    return inject(QrCodeService)
      .find(id)
      .pipe(
        mergeMap((qrCode: HttpResponse<IQrCode>) => {
          if (qrCode.body) {
            return of(qrCode.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default qrCodeResolve;
