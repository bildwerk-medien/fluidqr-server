import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GoogleUserDetailComponent } from './google-user-detail.component';

describe('GoogleUser Management Detail Component', () => {
  let comp: GoogleUserDetailComponent;
  let fixture: ComponentFixture<GoogleUserDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GoogleUserDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ googleUser: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(GoogleUserDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(GoogleUserDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load googleUser on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.googleUser).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
