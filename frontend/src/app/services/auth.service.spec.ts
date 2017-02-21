import {inject, TestBed} from '@angular/core/testing';
import {AuthService} from './auth.service';
import {MockBackend} from '@angular/http/testing';
import {BaseRequestOptions, Http} from '@angular/http';

describe('AuthService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AuthService,
        MockBackend,
        BaseRequestOptions,
        {
          provide: Http,
          useFactory: (backend: MockBackend, defaultOptions: BaseRequestOptions) => {
            return new Http(backend, defaultOptions);
          },
          deps: [MockBackend, BaseRequestOptions],
        }]
    });
  });

  it('should ...', inject([AuthService, MockBackend], (service: AuthService) => {
    expect(service).toBeTruthy();
  }));
});
