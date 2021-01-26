import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FluidQrServerTestModule } from '../../../test.module';
import { RedirectionDetailComponent } from 'app/entities/redirection/redirection-detail.component';
import { Redirection } from 'app/shared/model/redirection.model';

describe('Component Tests', () => {
  describe('Redirection Management Detail Component', () => {
    let comp: RedirectionDetailComponent;
    let fixture: ComponentFixture<RedirectionDetailComponent>;
    const route = ({ data: of({ redirection: new Redirection(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FluidQrServerTestModule],
        declarations: [RedirectionDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(RedirectionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RedirectionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load redirection on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.redirection).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
