import { IUser } from 'app/entities/user/user.model';

export interface IQrCode {
  id: number;
  code?: string | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewQrCode = Omit<IQrCode, 'id'> & { id: null };
