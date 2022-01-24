import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQrCode, getQrCodeIdentifier } from '../qr-code.model';

export type EntityResponseType = HttpResponse<IQrCode>;
export type EntityArrayResponseType = HttpResponse<IQrCode[]>;

@Injectable({ providedIn: 'root' })
export class QrCodeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/qr-codes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(qrCode: IQrCode): Observable<EntityResponseType> {
    return this.http.post<IQrCode>(this.resourceUrl, qrCode, { observe: 'response' });
  }

  update(qrCode: IQrCode): Observable<EntityResponseType> {
    return this.http.put<IQrCode>(`${this.resourceUrl}/${getQrCodeIdentifier(qrCode) as number}`, qrCode, { observe: 'response' });
  }

  partialUpdate(qrCode: IQrCode): Observable<EntityResponseType> {
    return this.http.patch<IQrCode>(`${this.resourceUrl}/${getQrCodeIdentifier(qrCode) as number}`, qrCode, { observe: 'response' });
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

  addQrCodeToCollectionIfMissing(qrCodeCollection: IQrCode[], ...qrCodesToCheck: (IQrCode | null | undefined)[]): IQrCode[] {
    const qrCodes: IQrCode[] = qrCodesToCheck.filter(isPresent);
    if (qrCodes.length > 0) {
      const qrCodeCollectionIdentifiers = qrCodeCollection.map(qrCodeItem => getQrCodeIdentifier(qrCodeItem)!);
      const qrCodesToAdd = qrCodes.filter(qrCodeItem => {
        const qrCodeIdentifier = getQrCodeIdentifier(qrCodeItem);
        if (qrCodeIdentifier == null || qrCodeCollectionIdentifiers.includes(qrCodeIdentifier)) {
          return false;
        }
        qrCodeCollectionIdentifiers.push(qrCodeIdentifier);
        return true;
      });
      return [...qrCodesToAdd, ...qrCodeCollection];
    }
    return qrCodeCollection;
  }
}
