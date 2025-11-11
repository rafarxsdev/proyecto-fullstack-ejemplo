import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      username: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit() {
    if (this.form.valid) {
      const credentials = this.form.value;
      console.log('Credenciales enviadas:', credentials);
      // TODO: llamar al servicio Auth para autenticación JWT
    } else {
      this.form.markAllAsTouched();
    }
  }

  onForgotPassword() {
    console.log('Recuperar contraseña');
  }
}
