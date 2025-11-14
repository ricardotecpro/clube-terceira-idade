import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { RouterModule } from '@angular/router'; // Importar RouterModule
import { CommonModule } from '@angular/common'; // Importar CommonModule

import { AppComponent } from "./app.component";
import { DashboardComponent } from './dashboard/dashboard.component';
import { AssociadosComponent } from './associados/associados.component';
import { CadastrarComponent } from './associados/cadastrar.component';
import { AssociadoDetalheComponent } from './associados/associado.component';
import { PagamentosComponent } from './pagamentos/pagamentos.component';
import { RelatoriosComponent } from './relatorios/relatorios.component';
import { EventosComponent } from './eventos/eventos.component';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { LoadingSpinnerComponent } from './loading-spinner/loading-spinner.component';
import { ToastComponent } from './toast/toast.component';
import { ConfirmationModalComponent } from './confirmation-modal/confirmation-modal.component';
import { MensagemComponent } from './mensagens/mensagem.component';
import { AdminComponent } from './admin/admin.component';
import { LayoutComponent } from './layout/layout.component';

import { AuthService } from './auth.service';
import { AuthGuard } from './auth.guard';
import { LoadingInterceptor } from './loading.interceptor';
import { AssociadosService } from './associados/associados.service';
import { DashboardService } from './dashboard/dashboard.service';
import { AdminService } from './admin/admin.service';

@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    AssociadosComponent,
    PagamentosComponent,
    RelatoriosComponent,
    EventosComponent,
    LoginComponent,
    CadastrarComponent,
    AssociadoDetalheComponent,
    HomeComponent,
    LoadingSpinnerComponent,
    ToastComponent,
    ConfirmationModalComponent,
    MensagemComponent,
    AdminComponent,
    LayoutComponent
  ],
  imports: [
    BrowserModule,
    FormsModule, // Necessário para [(ngModel)]
    ReactiveFormsModule, // Necessário para [formGroup]
    HttpClientModule,
    AppRoutingModule,
    RouterModule, // Necessário para router-outlet e routerLink
    CommonModule // Necessário para NgIf, NgFor, NgClass, DatePipe, DecimalPipe
  ],
  providers: [
    AuthService,
    AuthGuard,
    AssociadosService,
    DashboardService,
    AdminService,
    { provide: HTTP_INTERCEPTORS, useClass: LoadingInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
