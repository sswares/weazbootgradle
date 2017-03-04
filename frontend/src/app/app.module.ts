import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {HttpModule} from '@angular/http';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/take';
import 'rxjs/add/operator/do';

import {AppComponent} from './app.component';
import {HomeComponent} from './home/home.component';
import {ResourcesComponent} from './resources/resources.component';
import {LandingComponent} from './landing/landing.component';
import {CanActivateWhenLoggedInGuard} from './routes/can-activate-when-logged-in.guard';
import {routerConfig} from './routes/routes';
import {AuthService} from './services/auth.service';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    ResourcesComponent,
    LandingComponent
  ],
  imports: [
    BrowserModule,
    HttpModule,
    RouterModule.forRoot(routerConfig, {useHash: true})],
  providers: [AuthService, CanActivateWhenLoggedInGuard],
  bootstrap: [AppComponent]
})
export class AppModule {
}
