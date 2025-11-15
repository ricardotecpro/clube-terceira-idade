import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CidadesService } from './cidades.service';
import { NotificationService } from '../notification.service';
import { ConfirmationModalService } from '../confirmation-modal/confirmation-modal.service';

// Importações do Angular Material
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-cidades',
  templateUrl: './cidades.component.html',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatTableModule,
    MatIconModule
  ]
})
export class CidadesComponent implements OnInit {
  cidades: any[] = [];
  cidadeForm!: FormGroup;
  cidadeEmEdicaoId: number | null = null;
  displayedColumns: string[] = ['id', 'nome', 'estado', 'acoes'];

  constructor(
    private fb: FormBuilder,
    private cidadesService: CidadesService,
    private notificationService: NotificationService,
    private confirmationModalService: ConfirmationModalService
  ) { }

  ngOnInit(): void {
    this.cidadeForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(2)]],
      estado: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(2)]]
    });
    this.carregarCidades();
  }

  carregarCidades(): void {
    this.cidadesService.getCidades().subscribe({
      next: (data) => this.cidades = data,
      error: () => this.notificationService.error('Erro ao carregar cidades.')
    });
  }

  salvarCidade(): void {
    if (this.cidadeForm.invalid) {
      this.notificationService.warning('Por favor, preencha o formulário corretamente.');
      return;
    }

    const operacao = this.cidadeEmEdicaoId
      ? this.cidadesService.updateCidade(this.cidadeEmEdicaoId, this.cidadeForm.value)
      : this.cidadesService.createCidade(this.cidadeForm.value);

    const mensagemSucesso = this.cidadeEmEdicaoId ? 'Cidade atualizada com sucesso!' : 'Cidade criada com sucesso!';

    operacao.subscribe({
      next: () => {
        this.notificationService.success(mensagemSucesso);
        this.carregarCidades();
        this.resetarFormulario();
      },
      error: () => this.notificationService.error('Erro ao salvar cidade.')
    });
  }

  editarCidade(cidade: any): void {
    this.cidadeEmEdicaoId = cidade.id;
    this.cidadeForm.patchValue(cidade);
  }

  confirmarExclusao(id: number): void {
    this.confirmationModalService.confirm('Tem certeza que deseja excluir esta cidade?').subscribe(confirmado => {
      if (confirmado) {
        this.excluirCidade(id);
      }
    });
  }

  excluirCidade(id: number): void {
    this.cidadesService.deleteCidade(id).subscribe({
      next: () => {
        this.notificationService.success('Cidade excluída com sucesso!');
        this.carregarCidades();
      },
      error: () => this.notificationService.error('Erro ao excluir cidade. Verifique se ela não está sendo usada por um associado.')
    });
  }

  resetarFormulario(): void {
    this.cidadeForm.reset();
    this.cidadeEmEdicaoId = null;
  }
}
