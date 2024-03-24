import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IQrCode } from 'app/entities/qr-code/qr-code.model';

export interface IRedirection {
  id: number;
  description?: string | null;
  code?: string | null;
  url?: string | null;
  enabled?: boolean | null;
  creation?: dayjs.Dayjs | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id'> | null;
  qrCode?: Pick<IQrCode, 'id'> | null;
}

export type NewRedirection = Omit<IRedirection, 'id'> & { id: null };
