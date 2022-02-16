import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { GoogleUserService } from '../service/google-user.service';

import { GoogleUserComponent } from './google-user.component';

describe('GoogleUser Management Component', () => {
  let comp: GoogleUserComponent;
  let fixture: ComponentFixture<GoogleUserComponent>;
  let service: GoogleUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [GoogleUserComponent],
    })
      .overrideTemplate(GoogleUserComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GoogleUserComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(GoogleUserService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.googleUsers?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
