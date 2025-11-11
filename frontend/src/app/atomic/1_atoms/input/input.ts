import { Component, Input } from '@angular/core';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-input',
  standalone: false,
  templateUrl: './input.html',
  styleUrl: './input.scss',
})
export class InputComponent {
  @Input() placeholder = '';
  @Input() type = 'text';
  @Input() control!: FormControl;
  @Input() icon?: string;
}
