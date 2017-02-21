import {async, TestBed} from '@angular/core/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {Routes} from '@angular/router';
import {Observable} from 'rxjs/Rx';

import {AppComponent} from './app.component';
import {HomeComponent} from './home/home.component';
import {ResourcesComponent} from './resources/resources.component';
import {User} from './models/user';
import {AuthService} from './services/auth.service';

export const fakeRoutes: Routes = [
  {path: '', redirectTo: 'home', pathMatch: 'full'},
  {path: 'home', component: HomeComponent},
  {path: 'resources', component: ResourcesComponent}
];

class MockAuthService {
  getLoggedInUser(): Observable<User> {
    return Observable.of({});
  }
}

describe('AppComponent', () => {
  let fixture;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {provide: AuthService, useClass: MockAuthService}
      ],
      declarations: [
        AppComponent,
        HomeComponent,
        ResourcesComponent
      ],
      imports: [
        RouterTestingModule.withRoutes(fakeRoutes)
      ]
    });
    TestBed.compileComponents();
    fixture = TestBed.createComponent(AppComponent);
  });

  it('should render title in a h1 tag', async(() => {
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    const app = fixture.debugElement.componentInstance;
    console.log(app);
    expect(app.title).toEqual('Weazbootgradle');
    expect(compiled.querySelector('h1').textContent).toContain('Weazbootgradle');
  }));
});
