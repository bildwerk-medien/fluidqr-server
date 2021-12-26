import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { QrCodeDetailComponent } from './qr-code-detail.component';

describe('QrCode Management Detail Component', () => {
  let comp: QrCodeDetailComponent;
  let fixture: ComponentFixture<QrCodeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [QrCodeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ qrCode: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(QrCodeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(QrCodeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load qrCode on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.qrCode).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
