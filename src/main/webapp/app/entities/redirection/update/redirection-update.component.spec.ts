import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IQrCode } from 'app/entities/qr-code/qr-code.model';
import { QrCodeService } from 'app/entities/qr-code/service/qr-code.service';
import { IRedirection } from '../redirection.model';
import { RedirectionService } from '../service/redirection.service';
import { RedirectionFormService } from './redirection-form.service';

import { RedirectionUpdateComponent } from './redirection-update.component';

describe('Redirection Management Update Component', () => {
  let comp: RedirectionUpdateComponent;
  let fixture: ComponentFixture<RedirectionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let redirectionFormService: RedirectionFormService;
  let redirectionService: RedirectionService;
  let userService: UserService;
  let qrCodeService: QrCodeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), RedirectionUpdateComponent],
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
      .overrideTemplate(RedirectionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RedirectionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    redirectionFormService = TestBed.inject(RedirectionFormService);
    redirectionService = TestBed.inject(RedirectionService);
    userService = TestBed.inject(UserService);
    qrCodeService = TestBed.inject(QrCodeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const redirection: IRedirection = { id: 456 };
      const user: IUser = { id: 6060 };
      redirection.user = user;

      const userCollection: IUser[] = [{ id: 10612 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ redirection });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call QrCode query and add missing value', () => {
      const redirection: IRedirection = { id: 456 };
      const qrCode: IQrCode = { id: 8765 };
      redirection.qrCode = qrCode;

      const qrCodeCollection: IQrCode[] = [{ id: 8752 }];
      jest.spyOn(qrCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: qrCodeCollection })));
      const additionalQrCodes = [qrCode];
      const expectedCollection: IQrCode[] = [...additionalQrCodes, ...qrCodeCollection];
      jest.spyOn(qrCodeService, 'addQrCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ redirection });
      comp.ngOnInit();

      expect(qrCodeService.query).toHaveBeenCalled();
      expect(qrCodeService.addQrCodeToCollectionIfMissing).toHaveBeenCalledWith(
        qrCodeCollection,
        ...additionalQrCodes.map(expect.objectContaining),
      );
      expect(comp.qrCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const redirection: IRedirection = { id: 456 };
      const user: IUser = { id: 9328 };
      redirection.user = user;
      const qrCode: IQrCode = { id: 28149 };
      redirection.qrCode = qrCode;

      activatedRoute.data = of({ redirection });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.qrCodesSharedCollection).toContain(qrCode);
      expect(comp.redirection).toEqual(redirection);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRedirection>>();
      const redirection = { id: 123 };
      jest.spyOn(redirectionFormService, 'getRedirection').mockReturnValue(redirection);
      jest.spyOn(redirectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ redirection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: redirection }));
      saveSubject.complete();

      // THEN
      expect(redirectionFormService.getRedirection).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(redirectionService.update).toHaveBeenCalledWith(expect.objectContaining(redirection));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRedirection>>();
      const redirection = { id: 123 };
      jest.spyOn(redirectionFormService, 'getRedirection').mockReturnValue({ id: null });
      jest.spyOn(redirectionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ redirection: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: redirection }));
      saveSubject.complete();

      // THEN
      expect(redirectionFormService.getRedirection).toHaveBeenCalled();
      expect(redirectionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRedirection>>();
      const redirection = { id: 123 };
      jest.spyOn(redirectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ redirection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(redirectionService.update).toHaveBeenCalled();
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

    describe('compareQrCode', () => {
      it('Should forward to qrCodeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(qrCodeService, 'compareQrCode');
        comp.compareQrCode(entity, entity2);
        expect(qrCodeService.compareQrCode).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
