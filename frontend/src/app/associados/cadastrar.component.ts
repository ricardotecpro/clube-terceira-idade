import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Observable } from 'rxjs';
import { startWith, map, debounceTime, switchMap } from 'rxjs/operators';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';

import { NotificationService } from '../notification.service';
import { AssociadosService } from './associados.service';
import { CidadeService } from '../cidade/cidade.service';
import { AddCidadeDialogComponent } from '../cidade/add-cidade-dialog.component';

// Importações do Angular Material
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-cadastrar',
  templateUrl: './cadastrar.html',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatCardModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatAutocompleteModule,
    MatIconModule,
    AddCidadeDialogComponent // Importa o componente do diálogo
  ]
})
export class CadastrarComponent implements OnInit {
  associadoForm!: FormGroup;
  filteredCidades!: Observable<any[]>;

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private notificationService: NotificationService,
    private associadosService: AssociadosService,
    private cidadeService: CidadeService,
    public dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.associadoForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      cpf: ['', [Validators.required, Validators.pattern('^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$')]],
      dataNascimento: ['', [Validators.required]],
      situacao: ['Adimplente', [Validators.required]],
      cidade: [null, [Validators.required]] // Agora é um objeto
    });

    this.filteredCidades = this.associadoForm.get('cidade')!.valueChanges.pipe(
      startWith(''),
      debounceTime(300),
      switchMap(value => typeof value === 'string' ? this.cidadeService.buscarCidades(value) : [])
    );
  }

  displayCidade(cidade: any): string {
    return cidade && cidade.nome ? `${cidade.nome}, ${cidade.estado}` : '';
  }

  openAddCidadeDialog(): void {
    const dialogRef = this.dialog.open(AddCidadeDialogComponent, {
      width: '400px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.cidadeService.criarCidade(result).subscribe({
          next: (novaCidade) => {
            this.notificationService.success('Cidade adicionada com sucesso!');
            this.associadoForm.get('cidade')?.setValue(novaCidade);
          },
          error: (err) => {
            this.notificationService.error('Erro ao adicionar cidade.');
          }
        });
      }
    });
  }

  onSubmit() {
    if (this.associadoForm.valid) {
      const formValue = this.associadoForm.value;
      const payload = {
        ...formValue,
        cidadeId: formValue.cidade.id
      };
      delete payload.cidade;

      this.associadosService.createAssociado(payload).subscribe({
        next: () => {
          this.notificationService.success('Associado cadastrado com sucesso!');
          this.router.navigate(['/associados']);
        },
        error: (err) => {
          this.notificationService.error('Erro ao cadastrar associado.');
        }
      });
    } else {
      this.notificationService.warning('Por favor, preencha todos os campos obrigatórios corretamente.');
      this.associadoForm.markAllAsTouched();
    }
  }
}
