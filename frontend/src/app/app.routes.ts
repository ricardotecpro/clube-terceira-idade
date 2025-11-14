import { Routes } from '@angular/router';
import { AuthGuard } from './auth.guard';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { LayoutComponent } from './layout/layout.component';

export const routes: Routes = [
  { path: '', component: HomeComponent, pathMatch: 'full' },
  { path: 'login', loadComponent: () => import('./login/login.component').then(m => m.LoginComponent) }, // Lazy load
  {
    path: '',
    component: LayoutComponent,
    canActivate: [AuthGuard],
    children: [
      { path: 'dashboard', loadComponent: () => import('./dashboard/dashboard.component').then(m => m.DashboardComponent) }, // Lazy load
      {
        path: 'associados',
        children: [
          { path: '', loadComponent: () => import('./associados/associados.component').then(m => m.AssociadosComponent) }, // Lazy load
          { path: 'cadastrar', loadComponent: () => import('./associados/cadastrar.component').then(m => m.CadastrarComponent) }, // Lazy load
          { path: 'editar/:id', loadComponent: () => import('./associados/associado.component').then(m => m.AssociadoDetalheComponent) } // Lazy load
        ]
      },
      { path: 'pagamentos', loadComponent: () => import('./pagamentos/pagamentos.component').then(m => m.PagamentosComponent) }, // Lazy load
      { path: 'relatorios', loadComponent: () => import('./relatorios/relatorios.component').then(m => m.RelatoriosComponent) }, // Lazy load
      { path: 'eventos', loadComponent: () => import('./eventos/eventos.component').then(m => m.EventosComponent) }, // Lazy load
      { path: 'mensagens', loadComponent: () => import('./mensagens/mensagem.component').then(m => m.MensagemComponent) }, // Lazy load
      { path: 'admin', loadComponent: () => import('./admin/admin.component').then(m => m.AdminComponent) } // Lazy load
    ]
  },
  { path: '**', redirectTo: '' }
];
