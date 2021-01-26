import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IQrCode } from 'app/shared/model/qr-code.model';

type EntityResponseType = HttpResponse<IQrCode>;
type EntityArrayResponseType = HttpResponse<IQrCode[]>;

@Injectable({ providedIn: 'root' })
export class QrCodeService {
  public resourceUrl = SERVER_API_URL + 'api/qr-codes';

  constructor(protected http: HttpClient) {}

  create(qrCode: IQrCode): Observable<EntityResponseType> {
    return this.http.post<IQrCode>(this.resourceUrl, qrCode, { observe: 'response' });
  }

  update(qrCode: IQrCode): Observable<EntityResponseType> {
    return this.http.put<IQrCode>(this.resourceUrl, qrCode, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQrCode>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQrCode[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
