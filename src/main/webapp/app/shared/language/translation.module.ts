import { LOCALE_ID, NgModule } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { TranslateModule, TranslateService, TranslateLoader, MissingTranslationHandler } from '@ngx-translate/core';
import { translatePartialLoader, missingTranslationHandler } from 'app/config/translation.config';
import { SessionStorageService } from 'ngx-webstorage';

@NgModule({
  imports: [
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: translatePartialLoader,
        deps: [HttpClient],
      },
      missingTranslationHandler: {
        provide: MissingTranslationHandler,
        useFactory: missingTranslationHandler,
      },
    }),
  ],
})
export class TranslationModule {
  constructor(private translateService: TranslateService, sessionStorageService: SessionStorageService) {
    let browserLang = translateService.getBrowserLang();

    if (!browserLang) {
      browserLang = 'en';
    }

    translateService.setDefaultLang(browserLang);
    // if user have changed language and navigates away from the application and back to the application then use previously choosed language
    const langKey = sessionStorageService.retrieve('locale') ?? browserLang;
    translateService.use(langKey);
  }
}
