import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AniversariantesService } from './aniversariantes.service';
import { NotificationService } from '../notification.service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';

// Importações do Angular Material
import { MatCardModule } from '@angular/material/card';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { MensagemAniversarioDialogComponent } from './mensagem-aniversario-dialog.component';

@Component({
  selector: 'app-aniversariantes',
  templateUrl: './aniversariantes.component.html',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatButtonToggleModule,
    MatTableModule,
    MatIconModule,
    MatButtonModule,
    MatDialogModule
  ]
})
export class AniversariantesComponent implements OnInit {
  aniversariantes: any[] = [];
  periodoSelecionado: string = 'mes'; // 'dia', 'semana', 'mes'
  displayedColumns: string[] = ['nome', 'dataNascimento', 'contato', 'acoes'];

  constructor(
    private aniversariantesService: AniversariantesService,
    private notificationService: NotificationService,
    public dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.carregarAniversariantes();
  }

  carregarAniversariantes(): void {
    this.aniversariantesService.getAniversariantes(this.periodoSelecionado).subscribe({
      next: (data) => {
        this.aniversariantes = data;
      },
      error: () => {
        this.notificationService.error('Erro ao carregar aniversariantes.');
      }
    });
  }

  onPeriodoChange(): void {
    this.carregarAniversariantes();
  }

  abrirDialogoMensagem(associado: any): void {
    const dialogRef = this.dialog.open(MensagemAniversarioDialogComponent, {
      width: '500px',
      data: { associado }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'success') {
        this.notificationService.success(`Mensagem enviada para ${associado.nome}!`);
      } else if (result) {
        this.notificationService.error('Erro ao enviar mensagem.');
      }
    });
  }
}
