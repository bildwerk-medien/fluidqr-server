import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FluidQrServerTestModule } from '../../../test.module';
import { QrCodeDetailComponent } from 'app/entities/qr-code/qr-code-detail.component';
import { QrCode } from 'app/shared/model/qr-code.model';

describe('Component Tests', () => {
  describe('QrCode Management Detail Component', () => {
    let comp: QrCodeDetailComponent;
    let fixture: ComponentFixture<QrCodeDetailComponent>;
    const route = ({ data: of({ qrCode: new QrCode(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FluidQrServerTestModule],
        declarations: [QrCodeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
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
        expect(comp.qrCode).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
