import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NotificationService } from '../notification.service';
import { ConfirmationModalService } from '../confirmation-modal/confirmation-modal.service';

@Component({
  selector: 'app-associado-detalhe',
  templateUrl: './associado.html',
  styleUrls: ['./associado.css'],
  standalone: false
})
export class AssociadoDetalheComponent implements OnInit {

  associadoForm!: FormGroup;
  associadoId: number | null = null;
  mostrarModalRenovacao = false;
  novaValidadeCarteirinha: string = '';

  documentos: any[] = []; // Para listar documentos
  selectedFile: File | null = null; // Para o upload de arquivo

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
    private fb: FormBuilder,
    private notificationService: NotificationService,
    private confirmationModalService: ConfirmationModalService
  ) { }

  ngOnInit(): void {
    this.associadoForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      cpf: ['', [Validators.required, Validators.pattern('^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$')]],
      dataNascimento: ['', [Validators.required]],
      situacao: ['', [Validators.required]],
      cidadeId: [null, [Validators.required, Validators.min(1)]]
    });

    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.associadoId = +id;
        this.carregarAssociado(this.associadoId);
        this.carregarDocumentos(this.associadoId); // Carregar documentos
      }
    });
  }

  carregarAssociado(id: number): void {
    this.http.get<any>(`/api/associados/${id}`).subscribe({
      next: (data) => {
        this.associadoForm.patchValue({
          nome: data.nome,
          cpf: data.cpf,
          dataNascimento: data.dataNascimento,
          situacao: data.situacao,
          cidadeId: data.cidade ? data.cidade.id : null
        });
      },
      error: (error) => {
        this.notificationService.error('Erro ao carregar associado.');
        console.error('Erro ao carregar associado:', error);
        this.router.navigate(['/associados']);
      }
    });
  }

  onUpdate(): void {
    if (this.associadoForm.valid && this.associadoId) {
      const associadoAtualizado = { ...this.associadoForm.value };
      associadoAtualizado.cidade = { id: associadoAtualizado.cidadeId };
      delete associadoAtualizado.cidadeId;

      this.http.put(`/api/associados/${this.associadoId}`, associadoAtualizado).subscribe({
        next: (response) => {
          this.notificationService.success('Associado atualizado com sucesso!');
          this.router.navigate(['/associados']);
        },
        error: (error) => {
          this.notificationService.error('Erro ao atualizar associado.');
          console.error('Erro ao atualizar associado:', error);
        }
      });
    } else {
      this.notificationService.warning('Por favor, preencha todos os campos obrigatórios corretamente.');
      this.associadoForm.markAllAsTouched();
    }
  }

  abrirModalRenovacao(): void {
    this.mostrarModalRenovacao = true;
    this.novaValidadeCarteirinha = '';
  }

  fecharModalRenovacao(): void {
    this.mostrarModalRenovacao = false;
  }

  confirmRenovarCarteirinha(): void {
    if (!this.novaValidadeCarteirinha) {
      this.notificationService.warning('Por favor, selecione a nova data de validade.');
      return;
    }

    this.confirmationModalService.confirm(`Confirmar renovação da carteirinha para ${this.novaValidadeCarteirinha}?`).subscribe(confirmed => {
      if (confirmed) {
        this.renovarCarteirinha();
      }
    });
  }

  renovarCarteirinha(): void {
    if (this.associadoId && this.novaValidadeCarteirinha) {
      this.http.post(`/api/associados/${this.associadoId}/renovar-carteirinha?novaValidade=${this.novaValidadeCarteirinha}`, {}).subscribe({
        next: (response) => {
          this.notificationService.success('Carteirinha renovada com sucesso!');
          this.fecharModalRenovacao();
          this.carregarAssociado(this.associadoId!);
        },
        error: (error) => {
          this.notificationService.error('Erro ao renovar carteirinha.');
          console.error('Erro ao renovar carteirinha:', error);
        }
      });
    }
  }

  // Métodos para Documentos
  carregarDocumentos(associadoId: number): void {
    this.http.get<any[]>(`/api/documentos/associado/${associadoId}`).subscribe({
      next: (data) => {
        this.documentos = data;
      },
      error: (error) => {
        this.notificationService.error('Erro ao carregar documentos.');
        console.error('Erro ao carregar documentos:', error);
      }
    });
  }

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
  }

  uploadDocumento(): void {
    if (!this.selectedFile || !this.associadoId) {
      this.notificationService.warning('Selecione um arquivo e certifique-se de que o associado está carregado.');
      return;
    }

    const formData = new FormData();
    formData.append('file', this.selectedFile, this.selectedFile.name);

    this.http.post(`/api/documentos/upload/${this.associadoId}`, formData).subscribe({
      next: (response) => {
        this.notificationService.success('Documento enviado com sucesso!');
        this.selectedFile = null; // Limpa o arquivo selecionado
        this.carregarDocumentos(this.associadoId!); // Recarrega a lista de documentos
      },
      error: (error) => {
        this.notificationService.error('Erro ao enviar documento.');
        console.error('Erro ao enviar documento:', error);
      }
    });
  }

  downloadDocumento(fileName: string): void {
    window.open(`/api/documentos/download/${fileName}`, '_blank');
  }

  confirmDeleteDocumento(documentoId: number): void {
    this.confirmationModalService.confirm('Tem certeza que deseja excluir este documento?').subscribe(confirmed => {
      if (confirmed) {
        this.deleteDocumento(documentoId);
      }
    });
  }

  deleteDocumento(documentoId: number): void {
    this.http.delete(`/api/documentos/${documentoId}`).subscribe({
      next: () => {
        this.notificationService.success('Documento excluído com sucesso!');
        this.carregarDocumentos(this.associadoId!); // Recarrega a lista
      },
      error: (error) => {
        this.notificationService.error('Erro ao excluir documento.');
        console.error('Erro ao excluir documento:', error);
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/associados']);
  }
}
