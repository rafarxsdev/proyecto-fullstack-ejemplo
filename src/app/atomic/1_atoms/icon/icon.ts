import { Component, Input,  } from '@angular/core';
//import type { IconDefinition } from '@fortawesome/fontawesome-svg-core'; // opcional para FA

@Component({
  selector: 'app-icon',
  standalone: false,
  templateUrl: './icon.html',
  styleUrl: './icon.scss',
})
export class IconComponent {
  /**
   * type: 'material' | 'lucide' | 'fontawesome' | 'hero'
   * name: nombre del icono (según fuente)
   * size: número en px o string con clases (ej '24' o 'w-6 h-6')
   * color: CSS color o clase tailwind (ej: '#1976d2' o 'text-primary')
   * spin: si aplica animación giratoria (para FontAwesome o CSS)
   * faIcon: (opcional) IconDefinition si usas FontAwesome y pasas directamente la definición
   */
  @Input() type: 'material' | 'lucide' | 'fontawesome' | 'hero' = 'material';
  @Input() name = ''; 
  @Input() size: string | number = 20;
  @Input() color?: string;
  @Input() spin = false;
  @Input() ariaLabel?: string;
  //@Input() faIcon?: IconDefinition; // para FontAwesome: pasar IconDefinition desde el padre

  // helpers para template
  get sizePx(): string {
    return typeof this.size === 'number' ? `${this.size}px` : this.size;
  }

  constructor(){}

  ngOnInit(): void {}

}
