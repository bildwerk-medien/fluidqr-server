import { Moment } from 'moment';
import { IQrCode } from 'app/shared/model/qr-code.model';

export interface IRedirection {
  id?: number;
  description?: string;
  code?: string;
  url?: string;
  enabled?: boolean;
  creation?: Moment;
  startDate?: Moment;
  endDate?: Moment;
  qrCode?: IQrCode;
}

export class Redirection implements IRedirection {
  constructor(
    public id?: number,
    public description?: string,
    public code?: string,
    public url?: string,
    public enabled?: boolean,
    public creation?: Moment,
    public startDate?: Moment,
    public endDate?: Moment,
    public qrCode?: IQrCode
  ) {
    this.enabled = this.enabled || false;
  }
}
