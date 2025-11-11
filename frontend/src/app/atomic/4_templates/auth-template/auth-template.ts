import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-auth-template',
  standalone: false,
  templateUrl: './auth-template.html',
  styleUrl: './auth-template.scss',
})
export class AuthTemplateComponent {
  @Input() title = '';
  @Input() subtitle = '';
  @Input() logo?: string;
}
