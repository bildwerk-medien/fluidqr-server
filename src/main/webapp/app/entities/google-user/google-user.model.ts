import { IUser } from 'app/entities/user/user.model';

export interface IGoogleUser {
  id?: number;
  name?: string;
  refreshToken?: string | null;
  user?: IUser | null;
}

export class GoogleUser implements IGoogleUser {
  constructor(public id?: number, public name?: string, public refreshToken?: string | null, public user?: IUser | null) {}
}

export function getGoogleUserIdentifier(googleUser: IGoogleUser): number | undefined {
  return googleUser.id;
}
