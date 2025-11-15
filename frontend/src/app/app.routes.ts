import { Routes } from '@angular/router';
import { AuthGuard } from './auth.guard';
import { LayoutComponent } from './layout/layout.component';

export const routes: Routes = [
  { path: 'login', loadComponent: () => import('./login/login.component').then(m => m.LoginComponent) },
  {
    path: '',
    component: LayoutComponent,
    canActivate: [AuthGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }, // Redireciona a rota raiz para o dashboard
      { path: 'dashboard', loadComponent: () => import('./dashboard/dashboard.component').then(m => m.DashboardComponent) },
      {
        path: 'associados',
        children: [
          { path: '', loadComponent: () => import('./associados/associados.component').then(m => m.AssociadosComponent) },
          { path: 'cadastrar', loadComponent: () => import('./associados/cadastrar.component').then(m => m.CadastrarComponent) },
          { path: 'editar/:id', loadComponent: () => import('./associados/associado.component').then(m => m.AssociadoDetalheComponent) }
        ]
      },
      { path: 'cidades', loadComponent: () => import('./cidades/cidades.component').then(m => m.CidadesComponent) },
      { path: 'pagamentos', loadComponent: () => import('./pagamentos/pagamentos.component').then(m => m.PagamentosComponent) },
      { path: 'relatorios', loadComponent: () => import('./relatorios/relatorios.component').then(m => m.RelatoriosComponent) },
      { path: 'eventos', loadComponent: () => import('./eventos/eventos.component').then(m => m.EventosComponent) },
      { path: 'aniversariantes', loadComponent: () => import('./aniversariantes/aniversariantes.component').then(m => m.AniversariantesComponent) },
      { path: 'mensagens', loadComponent: () => import('./mensagens/mensagem.component').then(m => m.MensagemComponent) },
      { path: 'admin', loadComponent: () => import('./admin/admin.component').then(m => m.AdminComponent) }
    ]
  },
  { path: '**', redirectTo: '' }
];
