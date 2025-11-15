import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';

// Importações do Angular Material
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-mensagem-aniversario-dialog',
  templateUrl: './mensagem-aniversario-dialog.component.html',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule
  ]
})
export class MensagemAniversarioDialogComponent {
  mensagemForm: FormGroup;
  associado: any;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    public dialogRef: MatDialogRef<MensagemAniversarioDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.associado = data.associado;
    this.mensagemForm = this.fb.group({
      destinatarioId: [this.associado.id],
      assunto: ['Feliz Aniversário!', [Validators.required]],
      conteudo: [`Olá, ${this.associado.nome}! O Clube da Terceira Idade deseja a você um feliz aniversário, com muita saúde, paz e alegria!`, [Validators.required]],
      tipo: ['EMAIL', Validators.required] // 'EMAIL' ou 'SMS'
    });
  }

  enviarMensagem(): void {
    if (this.mensagemForm.invalid) {
      return;
    }

    this.http.post('/api/mensagens/aniversario', this.mensagemForm.value).subscribe({
      next: () => this.dialogRef.close('success'),
      error: () => this.dialogRef.close('error')
    });
  }

  cancelar(): void {
    this.dialogRef.close();
  }
}
