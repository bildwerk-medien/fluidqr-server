import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IRedirection } from 'app/shared/model/redirection.model';

type EntityResponseType = HttpResponse<IRedirection>;
type EntityArrayResponseType = HttpResponse<IRedirection[]>;

@Injectable({ providedIn: 'root' })
export class RedirectionService {
  public resourceUrl = SERVER_API_URL + 'api/redirections';

  constructor(protected http: HttpClient) {}

  create(redirection: IRedirection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(redirection);
    return this.http
      .post<IRedirection>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(redirection: IRedirection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(redirection);
    return this.http
      .put<IRedirection>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRedirection>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRedirection[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(redirection: IRedirection): IRedirection {
    const copy: IRedirection = Object.assign({}, redirection, {
      creation: redirection.creation && redirection.creation.isValid() ? redirection.creation.toJSON() : undefined,
      startDate: redirection.startDate && redirection.startDate.isValid() ? redirection.startDate.toJSON() : undefined,
      endDate: redirection.endDate && redirection.endDate.isValid() ? redirection.endDate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creation = res.body.creation ? moment(res.body.creation) : undefined;
      res.body.startDate = res.body.startDate ? moment(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? moment(res.body.endDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((redirection: IRedirection) => {
        redirection.creation = redirection.creation ? moment(redirection.creation) : undefined;
        redirection.startDate = redirection.startDate ? moment(redirection.startDate) : undefined;
        redirection.endDate = redirection.endDate ? moment(redirection.endDate) : undefined;
      });
    }
    return res;
  }
}
