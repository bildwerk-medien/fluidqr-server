import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQrCode, NewQrCode } from '../qr-code.model';

export type PartialUpdateQrCode = Partial<IQrCode> & Pick<IQrCode, 'id'>;

export type EntityResponseType = HttpResponse<IQrCode>;
export type EntityArrayResponseType = HttpResponse<IQrCode[]>;

@Injectable({ providedIn: 'root' })
export class QrCodeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/qr-codes');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(qrCode: NewQrCode): Observable<EntityResponseType> {
    return this.http.post<IQrCode>(this.resourceUrl, qrCode, { observe: 'response' });
  }

  update(qrCode: IQrCode): Observable<EntityResponseType> {
    return this.http.put<IQrCode>(`${this.resourceUrl}/${this.getQrCodeIdentifier(qrCode)}`, qrCode, { observe: 'response' });
  }

  partialUpdate(qrCode: PartialUpdateQrCode): Observable<EntityResponseType> {
    return this.http.patch<IQrCode>(`${this.resourceUrl}/${this.getQrCodeIdentifier(qrCode)}`, qrCode, { observe: 'response' });
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

  getQrCodeIdentifier(qrCode: Pick<IQrCode, 'id'>): number {
    return qrCode.id;
  }

  compareQrCode(o1: Pick<IQrCode, 'id'> | null, o2: Pick<IQrCode, 'id'> | null): boolean {
    return o1 && o2 ? this.getQrCodeIdentifier(o1) === this.getQrCodeIdentifier(o2) : o1 === o2;
  }

  addQrCodeToCollectionIfMissing<Type extends Pick<IQrCode, 'id'>>(
    qrCodeCollection: Type[],
    ...qrCodesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const qrCodes: Type[] = qrCodesToCheck.filter(isPresent);
    if (qrCodes.length > 0) {
      const qrCodeCollectionIdentifiers = qrCodeCollection.map(qrCodeItem => this.getQrCodeIdentifier(qrCodeItem)!);
      const qrCodesToAdd = qrCodes.filter(qrCodeItem => {
        const qrCodeIdentifier = this.getQrCodeIdentifier(qrCodeItem);
        if (qrCodeCollectionIdentifiers.includes(qrCodeIdentifier)) {
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
