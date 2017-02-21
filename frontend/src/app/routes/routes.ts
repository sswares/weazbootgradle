import {Routes} from '@angular/router';
import {HomeComponent} from '../home/home.component';
import {CanActivateWhenLoggedInGuard} from './can-activate-when-logged-in.guard';
import {LandingComponent} from '../landing/landing.component';
import {ResourcesComponent} from '../resources/resources.component';

export const routerConfig: Routes = [
  {path: '', component: HomeComponent, canActivate: [CanActivateWhenLoggedInGuard]},
  {path: 'landing', component: LandingComponent},
  {path: 'resources', component: ResourcesComponent, canActivate: [CanActivateWhenLoggedInGuard]}
];
