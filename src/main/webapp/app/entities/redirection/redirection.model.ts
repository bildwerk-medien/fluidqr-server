import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IQrCode } from 'app/entities/qr-code/qr-code.model';

export interface IRedirection {
  id?: number;
  description?: string | null;
  code?: string | null;
  url?: string;
  enabled?: boolean;
  creation?: dayjs.Dayjs | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  user?: IUser | null;
  qrCode?: IQrCode | null;
}

export class Redirection implements IRedirection {
  constructor(
    public id?: number,
    public description?: string | null,
    public code?: string | null,
    public url?: string,
    public enabled?: boolean,
    public creation?: dayjs.Dayjs | null,
    public startDate?: dayjs.Dayjs | null,
    public endDate?: dayjs.Dayjs | null,
    public user?: IUser | null,
    public qrCode?: IQrCode | null
  ) {
    this.enabled = this.enabled ?? false;
  }
}

export function getRedirectionIdentifier(redirection: IRedirection): number | undefined {
  return redirection.id;
}
