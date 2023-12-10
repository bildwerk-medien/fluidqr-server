import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { RedirectionDetailComponent } from './redirection-detail.component';

describe('Redirection Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RedirectionDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: RedirectionDetailComponent,
              resolve: { redirection: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(RedirectionDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load redirection on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RedirectionDetailComponent);

      // THEN
      expect(instance.redirection).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
