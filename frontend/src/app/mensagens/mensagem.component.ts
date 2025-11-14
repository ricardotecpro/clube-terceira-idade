import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MensagemService } from './mensagem.service';
import { AssociadosService } from '../associados/associados.service'; // Precisamos de um serviço para associados
import { NotificationService } from '../notification.service';

@Component({
  selector: 'app-mensagem',
  templateUrl: './mensagem.component.html',
  styleUrls: ['./mensagem.component.css'],
  standalone: false
})
export class MensagemComponent implements OnInit {
  mensagemForm!: FormGroup;
  associados: any[] = []; // Lista de associados para seleção
  tiposMensagem: string[] = ['EMAIL', 'SMS', 'SISTEMA'];

  constructor(
    private fb: FormBuilder,
    private mensagemService: MensagemService,
    private associadosService: AssociadosService, // Injetar
    private notificationService: NotificationService
  ) { }

  ngOnInit(): void {
    this.mensagemForm = this.fb.group({
      conteudo: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(500)]],
      tipo: ['EMAIL', [Validators.required]],
      destinatarioIds: [[], [Validators.required, Validators.minLength(1)]] // Array de IDs
    });

    this.carregarAssociados();
  }

  carregarAssociados(): void {
    this.associadosService.getAssociados().subscribe({ // Assumindo que AssociadosService tem um getAssociados
      next: (data) => {
        this.associados = data;
      },
      error: (error) => {
        this.notificationService.error('Erro ao carregar associados para seleção.');
        console.error('Erro ao carregar associados:', error);
      }
    });
  }

  onSubmit(): void {
    if (this.mensagemForm.valid) {
      this.mensagemService.enviarMensagem(this.mensagemForm.value).subscribe({
        next: (response) => {
          this.notificationService.success('Mensagem enviada com sucesso!');
          this.mensagemForm.reset({ tipo: 'EMAIL', destinatarioIds: [] });
        },
        error: (error) => {
          this.notificationService.error('Erro ao enviar mensagem.');
          console.error('Erro ao enviar mensagem:', error);
        }
      });
    } else {
      this.notificationService.warning('Por favor, preencha todos os campos obrigatórios corretamente.');
      this.mensagemForm.markAllAsTouched();
    }
  }
}
