import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DashboardComponent } from './dashboard/dashboard.component';
import { AssociadosComponent } from './associados/associados.component';
import { CadastrarComponent } from './associados/cadastrar.component';
import { AssociadoDetalheComponent } from './associados/associado.component';
import { PagamentosComponent } from './pagamentos/pagamentos.component';
import { RelatoriosComponent } from './relatorios/relatorios.component';
import { EventosComponent } from './eventos/eventos.component';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { MensagemComponent } from './mensagens/mensagem.component';
import { AdminComponent } from './admin/admin.component';
import { LayoutComponent } from './layout/layout.component'; // Importar LayoutComponent
import { AuthGuard } from './auth.guard';

const routes: Routes = [
  { path: '', component: HomeComponent, pathMatch: 'full' }, // Rota inicial para decidir login/dashboard
  { path: 'login', component: LoginComponent }, // Rota de login sem layout

  // Rotas protegidas que usam o LayoutComponent
  {
    path: '',
    component: LayoutComponent, // Componente de layout com a barra lateral
    canActivate: [AuthGuard],
    children: [
      { path: 'dashboard', component: DashboardComponent },
      {
        path: 'associados',
        children: [
          { path: '', component: AssociadosComponent },
          { path: 'cadastrar', component: CadastrarComponent },
          { path: 'editar/:id', component: AssociadoDetalheComponent }
        ]
      },
      { path: 'pagamentos', component: PagamentosComponent },
      { path: 'relatorios', component: RelatoriosComponent },
      { path: 'eventos', component: EventosComponent },
      { path: 'mensagens', component: MensagemComponent },
      { path: 'admin', component: AdminComponent }
    ]
  },

  { path: '**', redirectTo: '' } // Rota coringa
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
