import { IRedirection } from 'app/entities/redirection/redirection.model';
import { IUser } from 'app/entities/user/user.model';

export interface IQrCode {
  id?: number;
  code?: string;
  redirections?: IRedirection[] | null;
  user?: IUser | null;
  link?: string;
  currentRedirect?: string;
}

export class QrCode implements IQrCode {
  constructor(
    public id?: number,
    public code?: string,
    public redirections?: IRedirection[] | null,
    public user?: IUser | null,
    public link?: string,
    public currentRedirect?: string
  ) {}
}

export function getQrCodeIdentifier(qrCode: IQrCode): number | undefined {
  return qrCode.id;
}
