import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { routes } from './app/app.routes'; // Novo arquivo de rotas
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { importProvidersFrom } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

// Importar serviços globais
import { AuthService } from './app/auth.service';
import { AuthGuard } from './app/auth.guard';
import { LoadingInterceptor } from './app/loading.interceptor';
import { AssociadosService } from './app/associados/associados.service';
import { DashboardService } from './app/dashboard/dashboard.service';
import { AdminService } from './app/admin/admin.service';
import { NotificationService } from './app/notification.service';
import { ConfirmationModalService } from './app/confirmation-modal/confirmation-modal.service';
import { LoadingService } from './app/loading.service';

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes, withComponentInputBinding()), // Configura o roteador
    provideHttpClient(withInterceptors([LoadingInterceptor])), // Configura HttpClient com interceptor
    importProvidersFrom(
      BrowserAnimationsModule, // Necessário para Angular Material
      FormsModule, // Para ngModel
      ReactiveFormsModule // Para formGroup
    ),
    // Provedores de serviços globais
    AuthService,
    AuthGuard,
    AssociadosService,
    DashboardService,
    AdminService,
    NotificationService,
    ConfirmationModalService,
    LoadingService
  ]
}).catch(err => console.error(err));
