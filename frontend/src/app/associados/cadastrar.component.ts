import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms'; // Importar
import { NotificationService } from '../notification.service'; // Importar

@Component({
  selector: 'app-cadastrar',
  templateUrl: './cadastrar.html',
  standalone: false,
  styleUrls: ['./cadastrar.css']
})
export class CadastrarComponent implements OnInit {

  associadoForm!: FormGroup; // Usar FormGroup

  constructor(
    private http: HttpClient,
    private router: Router,
    private fb: FormBuilder, // Injetar FormBuilder
    private notificationService: NotificationService // Injetar
  ) { }

  ngOnInit(): void {
    this.associadoForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      cpf: ['', [Validators.required, Validators.pattern('^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$')]],
      dataNascimento: ['', [Validators.required]], // Validação de data pode ser mais complexa
      situacao: ['Adimplente', [Validators.required]],
      cidadeId: [null, [Validators.required, Validators.min(1)]]
    });
  }

  onSubmit() {
    if (this.associadoForm.valid) {
      console.log('Dados de cadastro enviados:', this.associadoForm.value);
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
      this.associadoForm.markAllAsTouched(); // Marca todos os campos como tocados para exibir erros
    }
  }
}
