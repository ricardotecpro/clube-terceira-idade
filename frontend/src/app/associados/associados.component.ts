import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormBuilder, FormGroup } from '@angular/forms'; // Importar
import { ConfirmationModalService } from '../confirmation-modal/confirmation-modal.service';
import { NotificationService } from '../notification.service';

@Component({
  selector: 'app-associados',
  templateUrl: './associados.html',
  styleUrls: ['./associados.css'],
  standalone: false
})
export class AssociadosComponent implements OnInit {
  associados: any[] = [];
  searchForm!: FormGroup; // Usar FormGroup para a busca

  constructor(
    private http: HttpClient,
    private fb: FormBuilder, // Injetar FormBuilder
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
