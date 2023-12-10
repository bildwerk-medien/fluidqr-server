import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { QrCodeService } from '../service/qr-code.service';
import { IQrCode } from '../qr-code.model';

import { QrCodeFormService } from './qr-code-form.service';

import { QrCodeUpdateComponent } from './qr-code-update.component';

describe('QrCode Management Update Component', () => {
  let comp: QrCodeUpdateComponent;
  let fixture: ComponentFixture<QrCodeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let qrCodeFormService: QrCodeFormService;
  let qrCodeService: QrCodeService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), QrCodeUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(QrCodeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QrCodeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    qrCodeFormService = TestBed.inject(QrCodeFormService);
    qrCodeService = TestBed.inject(QrCodeService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const qrCode: IQrCode = { id: 456 };
      const user: IUser = { id: 2368 };
      qrCode.user = user;

      const userCollection: IUser[] = [{ id: 1301 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ qrCode });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const qrCode: IQrCode = { id: 456 };
      const user: IUser = { id: 3903 };
      qrCode.user = user;

      activatedRoute.data = of({ qrCode });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.qrCode).toEqual(qrCode);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQrCode>>();
      const qrCode = { id: 123 };
      jest.spyOn(qrCodeFormService, 'getQrCode').mockReturnValue(qrCode);
      jest.spyOn(qrCodeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ qrCode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: qrCode }));
      saveSubject.complete();

      // THEN
      expect(qrCodeFormService.getQrCode).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(qrCodeService.update).toHaveBeenCalledWith(expect.objectContaining(qrCode));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQrCode>>();
      const qrCode = { id: 123 };
      jest.spyOn(qrCodeFormService, 'getQrCode').mockReturnValue({ id: null });
      jest.spyOn(qrCodeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ qrCode: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: qrCode }));
      saveSubject.complete();

      // THEN
      expect(qrCodeFormService.getQrCode).toHaveBeenCalled();
      expect(qrCodeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQrCode>>();
      const qrCode = { id: 123 };
      jest.spyOn(qrCodeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ qrCode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(qrCodeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
