import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IGoogleUser, GoogleUser } from '../google-user.model';
import { GoogleUserService } from '../service/google-user.service';

import { GoogleUserRoutingResolveService } from './google-user-routing-resolve.service';

describe('GoogleUser routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: GoogleUserRoutingResolveService;
  let service: GoogleUserService;
  let resultGoogleUser: IGoogleUser | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(GoogleUserRoutingResolveService);
    service = TestBed.inject(GoogleUserService);
    resultGoogleUser = undefined;
  });

  describe('resolve', () => {
    it('should return IGoogleUser returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGoogleUser = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultGoogleUser).toEqual({ id: 123 });
    });

    it('should return new IGoogleUser if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGoogleUser = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultGoogleUser).toEqual(new GoogleUser());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as GoogleUser })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGoogleUser = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultGoogleUser).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
