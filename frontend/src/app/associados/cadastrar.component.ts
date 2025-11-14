import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router'; // Importar RouterModule
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms'; // Importar ReactiveFormsModule
import { NotificationService } from '../notification.service';
import { CommonModule } from '@angular/common'; // Importar CommonModule

// Importar módulos Angular Material
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core'; // Para MatDatepicker

@Component({
  selector: 'app-cadastrar',
  templateUrl: './cadastrar.html',
  styleUrls: ['./cadastrar.css'],
  standalone: true, // AGORA É STANDALONE
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatCardModule,
    MatDatepickerModule,
    MatNativeDateModule
  ]
})
export class CadastrarComponent implements OnInit {

  associadoForm!: FormGroup;

  constructor(
    private http: HttpClient,
    private router: Router,
    private fb: FormBuilder,
    private notificationService: NotificationService
  ) { }

  ngOnInit(): void {
    this.associadoForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      cpf: ['', [Validators.required, Validators.pattern('^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$')]],
      dataNascimento: ['', [Validators.required]],
      situacao: ['Adimplente', [Validators.required]],
      cidadeId: [null, [Validators.required, Validators.min(1)]]
    });
  }

  onSubmit() {
    if (this.associadoForm.valid) {
      this.http.post('/api/associados', this.associadoForm.value).subscribe({
        next: (response) => {
          this.notificationService.success('Associado cadastrado com sucesso!');
          this.router.navigate(['/associados']);
        },
        error: (error) => {
          this.notificationService.error('Erro ao cadastrar associado.');
          console.error('Erro ao cadastrar associado:', error);
        }
      });
    } else {
      this.notificationService.warning('Por favor, preencha todos os campos obrigatórios corretamente.');
      this.associadoForm.markAllAsTouched();
    }
  }
}
