import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { AuthRoutingModule } from './auth-routing-module';

// importa el módulo atomic
import { AtomicModule } from '../../atomic/atomic-module'

import { Auth } from './auth';
import { Login } from './pages/login/login';


@NgModule({
  declarations: [
    Auth,
    Login
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    AtomicModule,
    AuthRoutingModule
  ]
})
export class AuthModule { }
