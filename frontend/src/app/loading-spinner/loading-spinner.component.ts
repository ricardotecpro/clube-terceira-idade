import { Component } from '@angular/core';
import { LoadingService } from '../loading.service';
import { CommonModule } from '@angular/common'; // Importar CommonModule

// Importar módulos Angular Material
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-loading-spinner',
  templateUrl: './loading-spinner.component.html',
  standalone: true,
  imports: [
    CommonModule,
    MatProgressSpinnerModule
  ]
})
export class LoadingSpinnerComponent {
  constructor(public loadingService: LoadingService) { }
}
