import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';

import { ButtonComponent } from './1_atoms/button/button';
import { InputComponent } from './1_atoms/input/input';
import { LabelComponent } from './1_atoms/label/label';
import { IconComponent } from './1_atoms/icon/icon';
import { FormFieldComponent } from './2_molecules/form-field/form-field';
import { LoginFormComponent } from './3_organisms/login-form/login-form';
import { AuthTemplateComponent } from './4_templates/auth-template/auth-template';
import { DesignShowcaseComponent } from './5_pages/design-showcase/design-showcase';

// Agregar solo para probar el sistema de diseño
import { RouterModule, Routes } from '@angular/router';
const atomicRoutes: Routes = [
  { 
    // Segmento restante: 'pages/design-showcase'
    path: 'pages/design-showcase', 
    component: DesignShowcaseComponent 
  }
];

@NgModule({
  declarations: [
    ButtonComponent,
    InputComponent,
    LabelComponent,
    IconComponent,
    FormFieldComponent,
    LoginFormComponent,
    AuthTemplateComponent,
    DesignShowcaseComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatInputModule,
    MatIconModule,
    MatFormFieldModule
    ,RouterModule.forChild(atomicRoutes)// Agregar solo para probar el sistema de diseño
  ],
  exports: [
    ButtonComponent,
    InputComponent,
    LabelComponent,
    FormFieldComponent,
    LoginFormComponent,
    AuthTemplateComponent,
    DesignShowcaseComponent
  ]
})
export class AtomicModule { }
