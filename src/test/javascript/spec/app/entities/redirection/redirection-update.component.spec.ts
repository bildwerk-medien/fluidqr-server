import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { FluidQrServerTestModule } from '../../../test.module';
import { RedirectionUpdateComponent } from 'app/entities/redirection/redirection-update.component';
import { RedirectionService } from 'app/entities/redirection/redirection.service';
import { Redirection } from 'app/shared/model/redirection.model';

describe('Component Tests', () => {
  describe('Redirection Management Update Component', () => {
    let comp: RedirectionUpdateComponent;
    let fixture: ComponentFixture<RedirectionUpdateComponent>;
    let service: RedirectionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FluidQrServerTestModule],
        declarations: [RedirectionUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(RedirectionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RedirectionUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RedirectionService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Redirection(123);
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
        const entity = new Redirection();
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
