import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { FluidQrServerTestModule } from '../../../test.module';
import { QrCodeUpdateComponent } from 'app/entities/qr-code/qr-code-update.component';
import { QrCodeService } from 'app/entities/qr-code/qr-code.service';
import { QrCode } from 'app/shared/model/qr-code.model';

describe('Component Tests', () => {
  describe('QrCode Management Update Component', () => {
    let comp: QrCodeUpdateComponent;
    let fixture: ComponentFixture<QrCodeUpdateComponent>;
    let service: QrCodeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FluidQrServerTestModule],
        declarations: [QrCodeUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(QrCodeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(QrCodeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(QrCodeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new QrCode(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new QrCode();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
