import {Component, OnInit} from '@angular/core';
import {User} from './models/user';
import {AuthService} from './services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {

  private errorMessage: any;
  user: User;
  title = 'Weazbootgradle';

  constructor(private authService: AuthService) {
  }

  ngOnInit(): void {
    this.getUser();
  }

  getUser() {
    this.authService.getLoggedInUser().subscribe(
      user => this.user = user,
      error => this.errorMessage = <any>error);
  }
}
