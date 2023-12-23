import { IUser } from 'app/entities/user/user.model';

export interface IQrCode {
  id: number;
  code?: string | null;
  user?: Pick<IUser, 'id'> | null;

  //    custom transient fields ### start ###
  currentRedirect?: string;
  link?: string;
}

export type NewQrCode = Omit<IQrCode, 'id'> & { id: null };
