import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { FluidQrServerSharedModule } from 'app/shared/shared.module';
import { FluidQrServerCoreModule } from 'app/core/core.module';
import { FluidQrServerAppRoutingModule } from './app-routing.module';
import { FluidQrServerHomeModule } from './home/home.module';
import { FluidQrServerEntityModule } from './entities/entity.module';
import { FluidQrServerAppQrCodesModule } from './qr-codes/qr-codes.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    FluidQrServerSharedModule,
    FluidQrServerCoreModule,
    FluidQrServerHomeModule,
    FluidQrServerAppQrCodesModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    FluidQrServerEntityModule,
    FluidQrServerAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [MainComponent],
})
export class FluidQrServerAppModule {}
