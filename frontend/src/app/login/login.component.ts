import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms'; // Importar
import { NotificationService } from '../notification.service'; // Importar

@Component({
  selector: 'app-login',
  templateUrl: './login.html',
  styleUrls: ['./login.css'],
  standalone: false
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup; // Usar FormGroup
  loginError = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private fb: FormBuilder, // Injetar FormBuilder
    private notificationService: NotificationService // Injetar
  ) { }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  fazerLogin() {
    this.loginError = false; // Resetar erro ao tentar login
    if (this.loginForm.valid) {
      this.authService.login(this.loginForm.value)
        .subscribe(success => {
          if (!success) {
            this.loginError = true;
            this.notificationService.error('Usuário ou senha inválidos.');
          } else {
            this.notificationService.success('Login realizado com sucesso!');
          }
        });
    } else {
      this.notificationService.warning('Por favor, preencha o usuário e a senha.');
      this.loginForm.markAllAsTouched();
    }
  }
}
