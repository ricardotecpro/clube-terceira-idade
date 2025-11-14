import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ConfirmationModalService } from '../confirmation-modal/confirmation-modal.service';
import { NotificationService } from '../notification.service';

@Component({
  selector: 'app-eventos',
  templateUrl: './eventos.html',
  styleUrl: './eventos.css',
  standalone: false
})
export class EventosComponent implements OnInit {
  eventos: any[] = [];
  eventoForm!: FormGroup;
  eventoEmEdicaoId: number | null = null;
  mostrarFormularioCriacao = false;

  constructor(
    private http: HttpClient,
    private fb: FormBuilder,
    private confirmationModalService: ConfirmationModalService,
    private notificationService: NotificationService
  ) { }

  ngOnInit(): void {
    this.eventoForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      data: ['', [Validators.required]],
      hora: ['', [Validators.required]], // Novo campo
      local: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]], // Novo campo
      descricao: ['', [Validators.maxLength(500)]],
      participantes: ['', [Validators.maxLength(255)]] // Novo campo
    });
    this.fetchEventos();
  }

  fetchEventos(): void {
    this.http.get<any[]>('/api/eventos').subscribe({
      next: (data) => {
        this.eventos = data;
      },
      error: (error) => {
        this.notificationService.error('Erro ao carregar eventos.');
        console.error('Erro ao buscar eventos:', error);
      }
    });
  }

  abrirFormularioCriacao(): void {
    this.mostrarFormularioCriacao = true;
    this.eventoForm.reset({ nome: '', data: '', hora: '', local: '', descricao: '', participantes: '' }); // Limpa o formulário
    this.eventoEmEdicaoId = null;
  }

  fecharFormulario(): void {
    this.mostrarFormularioCriacao = false;
    this.eventoEmEdicaoId = null;
  }

  criarOuAtualizarEvento(): void {
    if (this.eventoForm.valid) {
      const eventoData = { ...this.eventoForm.value };
      if (eventoData.data) {
        eventoData.data = new Date(eventoData.data).toISOString().split('T')[0];
      }
      // A hora já deve estar no formato HH:mm, o backend espera LocalTime

      if (this.eventoEmEdicaoId) {
        // Atualizar
        this.http.put(`/api/eventos/${this.eventoEmEdicaoId}`, eventoData).subscribe({
          next: (response) => {
            this.notificationService.success('Evento atualizado com sucesso!');
            this.fetchEventos();
            this.fecharFormulario();
          },
          error: (error) => {
            this.notificationService.error('Erro ao atualizar evento.');
            console.error('Erro ao atualizar evento:', error);
          }
        });
      } else {
        // Criar
        this.http.post('/api/eventos', eventoData).subscribe({
          next: (response) => {
            this.notificationService.success('Evento criado com sucesso!');
            this.fetchEventos();
            this.fecharFormulario();
          },
          error: (error) => {
            this.notificationService.error('Erro ao criar evento.');
            console.error('Erro ao criar evento:', error);
          }
        });
      }
    } else {
      this.notificationService.warning('Por favor, preencha todos os campos obrigatórios corretamente.');
      this.eventoForm.markAllAsTouched();
    }
  }

  prepararEdicao(evento: any): void {
    this.eventoEmEdicaoId = evento.id;
    this.eventoForm.patchValue({
      nome: evento.nome,
      data: evento.data,
      hora: evento.hora, // Preencher hora
      local: evento.local, // Preencher local
      descricao: evento.descricao,
      participantes: evento.participantes // Preencher participantes
    });
    this.mostrarFormularioCriacao = true;
  }

  confirmDeleteEvento(id: number): void {
    this.confirmationModalService.confirm('Tem certeza que deseja excluir este evento?').subscribe(confirmed => {
      if (confirmed) {
        this.deleteEvento(id);
      }
    });
  }

  deleteEvento(id: number): void {
    this.http.delete(`/api/eventos/${id}`).subscribe({
      next: () => {
        this.notificationService.success('Evento excluído com sucesso!');
        this.eventos = this.eventos.filter(evento => evento.id !== id);
      },
      error: (error) => {
        this.notificationService.error('Erro ao excluir evento.');
        console.error('Erro ao excluir evento:', error);
      }
    });
  }
}
