import { IQrCode, NewQrCode } from './qr-code.model';

export const sampleWithRequiredData: IQrCode = {
  id: 16762,
  code: 'overconfidently nail minus',
};

export const sampleWithPartialData: IQrCode = {
  id: 31537,
  code: 'whose hint ouch',
};

export const sampleWithFullData: IQrCode = {
  id: 16007,
  code: 'surprisingly dishonest',
};

export const sampleWithNewData: NewQrCode = {
  code: 'joyfully mailbox boohoo',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
