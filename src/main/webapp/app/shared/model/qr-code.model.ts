import { IRedirection } from 'app/shared/model/redirection.model';
import { IUser } from 'app/core/user/user.model';

export interface IQrCode {
  id?: number;
  code?: string;
  redirections?: IRedirection[];
  user?: IUser;
}

export class QrCode implements IQrCode {
  constructor(public id?: number, public code?: string, public redirections?: IRedirection[], public user?: IUser) {}
}
