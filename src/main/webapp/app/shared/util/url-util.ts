// custom class
import { Injectable } from '@angular/core';

@Injectable()
export class UrlUtil {
  enhanceToHttps(url: string | undefined): string {
    if (url?.startsWith('http')) {
      return url;
    }

    if (!url) {
      return '';
    }

    return `https://${url}`;
  }
}
