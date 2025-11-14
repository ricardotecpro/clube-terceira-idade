import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms'; // Importar ReactiveFormsModule
import { ConfirmationModalService } from '../confirmation-modal/confirmation-modal.service';
import { NotificationService } from '../notification.service';
import { CommonModule } from '@angular/common'; // Importar CommonModule
import { RouterModule } from '@angular/router'; // Importar RouterModule

// Importar módulos Angular Material
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-associados',
  templateUrl: './associados.html',
  styleUrls: ['./associados.css'],
  standalone: true, // AGORA É STANDALONE
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatSelectModule
  ]
})
export class AssociadosComponent implements OnInit {
  associados: any[] = [];
  searchForm!: FormGroup;
  displayedColumns: string[] = ['id', 'nome', 'cpf', 'situacao', 'acoes']; // Colunas para MatTable

  constructor(
    private http: HttpClient,
    private fb: FormBuilder,
    private confirmationModalService: ConfirmationModalService,
    private notificationService: NotificationService
  ) { }

  ngOnInit(): void {
    this.searchForm = this.fb.group({
      nome: [''],
      cpf: [''],
      situacao: [''],
      bairro: [''],
      escolaridade: ['']
    });
    this.fetchAssociados();
  }

  fetchAssociados(): void {
    const filters = this.searchForm.value;
    let params = new URLSearchParams();

    for (const key in filters) {
      if (filters[key]) {
        params.append(key, filters[key]);
      }
    }

    this.http.get<any[]>(`/api/associados?${params.toString()}`).subscribe({
      next: (data) => {
        this.associados = data;
      },
      error: (error) => {
        this.notificationService.error('Erro ao carregar associados.');
        console.error('Erro ao carregar associados:', error);
      }
    });
  }

  buscar(): void {
    this.fetchAssociados();
  }

  confirmDeleteAssociado(id: number): void {
    this.confirmationModalService.confirm('Tem certeza que deseja excluir este associado?').subscribe(confirmed => {
      if (confirmed) {
        this.deleteAssociado(id);
      }
    });
  }

  deleteAssociado(id: number): void {
    this.http.delete(`/api/associados/${id}`).subscribe({
      next: () => {
        this.notificationService.success('Associado excluído com sucesso!');
        this.fetchAssociados();
      },
      error: (error) => {
        this.notificationService.error('Erro ao excluir associado.');
        console.error('Erro ao excluir associado:', error);
      }
    });
  }
}
