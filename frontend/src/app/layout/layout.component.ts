import { Component } from '@angular/core';
import { Router, RouterOutlet, RouterModule } from '@angular/router'; // Importar RouterModule
import { AuthService } from '../auth.service';
import { CommonModule } from '@angular/common'; // Importar CommonModule

// Importar módulos Angular Material
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.css'],
  standalone: true, // AGORA É STANDALONE
  imports: [
    CommonModule,
    RouterOutlet,
    RouterModule, // Para routerLink e routerLinkActive
    MatSidenavModule,
    MatToolbarModule,
    MatListModule,
    MatIconModule,
    MatButtonModule
  ]
})
export class LayoutComponent {
  constructor(private authService: AuthService, private router: Router) {}

  logout(): void {
    this.authService.logout();
  }
}
