import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import 'rxjs/add/operator/toPromise';
import {Observable} from 'rxjs/Rx';
import {User} from '../models/user';

@Injectable()
export class AuthService {

  constructor(private http: Http) {
  }

  getLoggedInUser(): Observable<User> {
    return this.http.get('user')
      .map(this.extractData)
      .catch(this.handleError);
  }

  private extractData(response: Response) {
    if (response.url.includes('auth/login')) {
      return null;
    }
    return response.json() as User;
  }

  private handleError(error: Response | any) {
    let errMsg: string;
    if (error instanceof Response) {
      const body = error.json() || '';
      const err = body.error || JSON.stringify(body);
      errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
    } else {
      errMsg = error.message ? error.message : error.toString();
    }
    console.error(errMsg);
    return Observable.throw(errMsg);
  }
}
