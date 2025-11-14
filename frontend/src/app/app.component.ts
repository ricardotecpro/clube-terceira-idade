import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router'; // Importar RouterOutlet
import { LoadingSpinnerComponent } from './loading-spinner/loading-spinner.component';
import { ToastComponent } from './toast/toast.component';
import { ConfirmationModalComponent } from './confirmation-modal/confirmation-modal.component';
import { CommonModule } from '@angular/common'; // Necessário para NgIf, NgFor, NgClass

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  standalone: true, // AGORA É STANDALONE
  imports: [
    RouterOutlet,
    LoadingSpinnerComponent,
    ToastComponent,
    ConfirmationModalComponent,
    CommonModule // Para *ngIf, *ngFor, etc. se usados no template do AppComponent
  ]
})
export class AppComponent {
  title = 'Cadastro de Associados';
  // A lógica de isLoginPage e logout foi movida para o LayoutComponent
  // ou para um serviço de autenticação que o LayoutComponent consome.
  // AppComponent agora é um container mais simples.
}
