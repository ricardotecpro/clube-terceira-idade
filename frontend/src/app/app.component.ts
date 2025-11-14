import { Component } from '@angular/core';
import { RouterOutlet, Router, NavigationEnd } from '@angular/router'; // Importar NavigationEnd
import { AuthService } from './auth.service';
import { filter } from 'rxjs/operators'; // Importar filter

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  standalone: false
})
export class AppComponent {
  title = 'Cadastro de Associados';
  isLoginPage: boolean = false; // Nova propriedade

  constructor(private authService: AuthService, private router: Router) {
    // Escuta as mudanças de rota para saber se estamos na página de login
    this.router.events.pipe(
      filter((event): event is NavigationEnd => event instanceof NavigationEnd)
    ).subscribe((event: NavigationEnd) => {
      this.isLoginPage = event.url === '/login';
    });
  }

  logout(): void {
    this.authService.logout();
  }
}
