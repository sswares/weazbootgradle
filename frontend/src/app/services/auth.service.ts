import {Injectable} from '@angular/core';
import {Http} from '@angular/http';
import 'rxjs/add/operator/toPromise';
import {Observable} from 'rxjs/Observable';
import {ReplaySubject} from 'rxjs/ReplaySubject';
import {User} from '../models/user';

@Injectable()
export class AuthService {
  private dataObs$ = new ReplaySubject(1);

  constructor(private http: Http) {
  }

  getLoggedInUser(): Observable<User> {
    if (!this.dataObs$.observers.length) {
      this.http.get('user').subscribe(
        data => {
          if (data.url.includes('auth/login')) {
            this.dataObs$.next(null);
          } else {
            this.dataObs$.next(data);
          }
        },
        error => {
          this.dataObs$.error(error);
          this.dataObs$ = new ReplaySubject(1);
        });
    }
    return this.dataObs$;
  }
}
