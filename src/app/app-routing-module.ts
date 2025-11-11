import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: 'auth', 
    loadChildren: () => import('./features/auth/auth-module').then(m => m.AuthModule) 
  }
  // Agregar solo para probar el sistema de diseño
  ,{ 
    path: 'atomic', 
    loadChildren: () => import('./atomic/atomic-module').then(m => m.AtomicModule)
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
