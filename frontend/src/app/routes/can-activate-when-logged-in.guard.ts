import {Injectable} from '@angular/core';
import {CanActivate, Router} from '@angular/router';
import {Observable} from 'rxjs/Rx';
import {AuthService} from '../services/auth.service';

@Injectable()
export class CanActivateWhenLoggedInGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {
  }

  canActivate() {
    return this.authService.getLoggedInUser().map(result => {
      if (result) {
        return true;
      } else {
        this.router.navigate(['landing']);
        return false;
      }
    }).catch(() => {
      return Observable.of(false);
    });
  }
}
