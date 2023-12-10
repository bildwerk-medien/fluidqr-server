import dayjs from 'dayjs/esm';

import { IRedirection, NewRedirection } from './redirection.model';

export const sampleWithRequiredData: IRedirection = {
  id: 2116,
  url: 'https://each-base.net',
  enabled: true,
};

export const sampleWithPartialData: IRedirection = {
  id: 25178,
  description: 'athwart summit',
  url: 'https://astonishing-jackal.name',
  enabled: true,
  creation: dayjs('2021-01-26T09:38'),
  startDate: dayjs('2021-01-25T22:18'),
  endDate: dayjs('2021-01-26T15:20'),
};

export const sampleWithFullData: IRedirection = {
  id: 7837,
  description: 'aw',
  code: 'unaware illness deform',
  url: 'https://harmless-watcher.org',
  enabled: false,
  creation: dayjs('2021-01-26T06:19'),
  startDate: dayjs('2021-01-26T18:21'),
  endDate: dayjs('2021-01-26T05:43'),
};

export const sampleWithNewData: NewRedirection = {
  url: 'https://threadbare-sneeze.com/',
  enabled: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
