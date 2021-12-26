import { IRedirection } from 'app/entities/redirection/redirection.model';
import { IUser } from 'app/entities/user/user.model';

export interface IQrCode {
  id?: number;
  code?: string;
  redirections?: IRedirection[] | null;
  user?: IUser | null;
}

export class QrCode implements IQrCode {
  constructor(public id?: number, public code?: string, public redirections?: IRedirection[] | null, public user?: IUser | null) {}
}

export function getQrCodeIdentifier(qrCode: IQrCode): number | undefined {
  return qrCode.id;
}
