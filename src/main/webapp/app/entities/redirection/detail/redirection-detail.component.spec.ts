import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RedirectionDetailComponent } from './redirection-detail.component';

describe('Redirection Management Detail Component', () => {
  let comp: RedirectionDetailComponent;
  let fixture: ComponentFixture<RedirectionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RedirectionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ redirection: { id: 123 } }) },
        },
      ],
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
      expect(comp.redirection).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
