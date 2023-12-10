import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { QrCodeDetailComponent } from './qr-code-detail.component';

describe('QrCode Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QrCodeDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: QrCodeDetailComponent,
              resolve: { qrCode: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(QrCodeDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load qrCode on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', QrCodeDetailComponent);

      // THEN
      expect(instance.qrCode).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
