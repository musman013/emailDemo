
import { RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders } from "@angular/core";
import { CanDeactivateGuard } from 'projects/fast-code-core/src/public_api';
import { HomeComponent } from './home/home.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { SwaggerComponent } from 'src/app/swagger/swagger.component';
import { ErrorPageComponent  } from './error-page/error-page.component';
import { EmailRoutes } from 'projects/ip-email-builder/src/public_api';

import { TaskListComponent, TaskDetailsComponent, TaskNewComponent } from './task/index';

const routes: Routes = [
	
	{ path: 'dashboard',  component: DashboardComponent   },
  { path: "swagger-ui", component: SwaggerComponent },
	{ path: 'task', component: TaskListComponent, canDeactivate: [CanDeactivateGuard]},
	{ path: 'task/new', component: TaskNewComponent },
	{ path: 'task/:id', component: TaskDetailsComponent, canDeactivate: [CanDeactivateGuard]},
  {path: 'email', children: EmailRoutes },
  { path: '', component: HomeComponent },
  //{ path: '', redirectTo: '/', pathMatch: 'full' },
  { path: '**', component:ErrorPageComponent},
	
];

export const routingModule: ModuleWithProviders = RouterModule.forRoot(routes);