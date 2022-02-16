import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IGoogleUser {
  id?: number;
  name?: string;
  refreshToken?: string | null;
  enabled?: boolean | null;
  creationTime?: dayjs.Dayjs | null;
  user?: IUser | null;
}

export class GoogleUser implements IGoogleUser {
  constructor(
    public id?: number,
    public name?: string,
    public refreshToken?: string | null,
    public enabled?: boolean | null,
    public creationTime?: dayjs.Dayjs | null,
    public user?: IUser | null
  ) {
    this.enabled = this.enabled ?? false;
  }
}

export function getGoogleUserIdentifier(googleUser: IGoogleUser): number | undefined {
  return googleUser.id;
}
