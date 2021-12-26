jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RedirectionService } from '../service/redirection.service';
import { IRedirection, Redirection } from '../redirection.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IQrCode } from 'app/entities/qr-code/qr-code.model';
import { QrCodeService } from 'app/entities/qr-code/service/qr-code.service';

import { RedirectionUpdateComponent } from './redirection-update.component';

describe('Redirection Management Update Component', () => {
  let comp: RedirectionUpdateComponent;
  let fixture: ComponentFixture<RedirectionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let redirectionService: RedirectionService;
  let userService: UserService;
  let qrCodeService: QrCodeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RedirectionUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(RedirectionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RedirectionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    redirectionService = TestBed.inject(RedirectionService);
    userService = TestBed.inject(UserService);
    qrCodeService = TestBed.inject(QrCodeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const redirection: IRedirection = { id: 456 };
      const user: IUser = { id: 42880 };
      redirection.user = user;

      const userCollection: IUser[] = [{ id: 78393 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ redirection });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call QrCode query and add missing value', () => {
      const redirection: IRedirection = { id: 456 };
      const qrCode: IQrCode = { id: 92489 };
      redirection.qrCode = qrCode;

      const qrCodeCollection: IQrCode[] = [{ id: 54499 }];
      jest.spyOn(qrCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: qrCodeCollection })));
      const additionalQrCodes = [qrCode];
      const expectedCollection: IQrCode[] = [...additionalQrCodes, ...qrCodeCollection];
      jest.spyOn(qrCodeService, 'addQrCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ redirection });
      comp.ngOnInit();

      expect(qrCodeService.query).toHaveBeenCalled();
      expect(qrCodeService.addQrCodeToCollectionIfMissing).toHaveBeenCalledWith(qrCodeCollection, ...additionalQrCodes);
      expect(comp.qrCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const redirection: IRedirection = { id: 456 };
      const user: IUser = { id: 2438 };
      redirection.user = user;
      const qrCode: IQrCode = { id: 48367 };
      redirection.qrCode = qrCode;

      activatedRoute.data = of({ redirection });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(redirection));
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.qrCodesSharedCollection).toContain(qrCode);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Redirection>>();
      const redirection = { id: 123 };
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
      expect(comp.previousState).toHaveBeenCalled();
      expect(redirectionService.update).toHaveBeenCalledWith(redirection);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Redirection>>();
      const redirection = new Redirection();
      jest.spyOn(redirectionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ redirection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: redirection }));
      saveSubject.complete();

      // THEN
      expect(redirectionService.create).toHaveBeenCalledWith(redirection);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Redirection>>();
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
      expect(redirectionService.update).toHaveBeenCalledWith(redirection);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUserById', () => {
      it('Should return tracked User primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackQrCodeById', () => {
      it('Should return tracked QrCode primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackQrCodeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
