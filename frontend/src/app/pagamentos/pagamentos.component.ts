import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms'; // Importar
import { NotificationService } from '../notification.service'; // Importar
import { ConfirmationModalService } from '../confirmation-modal/confirmation-modal.service'; // Importar
import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-pagamentos',
    templateUrl: './pagamentos.html',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule]
})
export class PagamentosComponent implements OnInit {
    pagamentos: any[] = [];
    pagamentoForm!: FormGroup; // Usar FormGroup para o novo pagamento
    mostrarFormularioPagamento = false;
    associadosDisponiveis: any[] = [];

    constructor(
        private http: HttpClient,
        private router: Router,
        private fb: FormBuilder, // Injetar FormBuilder
        private notificationService: NotificationService, // Injetar
        private confirmationModalService: ConfirmationModalService // Injetar
    ) { }

    ngOnInit(): void {
        this.pagamentoForm = this.fb.group({
            associadoId: [null, [Validators.required, Validators.min(1)]],
            valor: [null, [Validators.required, Validators.min(0.01)]],
            dataPagamento: ['', [Validators.required]],
            tipoPagamento: ['Mensalidade'],
            formaPagamento: [null]
        });

        this.fetchPagamentos();
        this.fetchAssociados();
    }

    fetchPagamentos(): void {
        this.http.get<any[]>('/api/pagamentos').subscribe({
            next: (data) => {
                this.pagamentos = data;
            },
            error: (error) => {
                this.notificationService.error('Erro ao buscar pagamentos.');
                console.error('Erro ao buscar pagamentos:', error);
            }
        });
    }

    fetchAssociados(): void {
        this.http.get<any[]>('/api/associados').subscribe({
            next: (data) => {
                this.associadosDisponiveis = data;
            },
            error: (error) => {
                this.notificationService.error('Erro ao buscar associados para o formulário.');
                console.error('Erro ao buscar associados:', error);
            }
        });
    }

    abrirFormularioPagamento(): void {
        this.mostrarFormularioPagamento = true;
        this.pagamentoForm.reset({
            tipoPagamento: 'Mensalidade',
            formaPagamento: null
        });
    }

    fecharFormularioPagamento(): void {
        this.mostrarFormularioPagamento = false;
    }

    lancarPagamento(): void {
        if (this.pagamentoForm.valid) {
            const novoPagamentoData = { ...this.pagamentoForm.value };
            // Ajustar a data para o formato esperado pelo backend (LocalDate)
            if (novoPagamentoData.dataPagamento) {
                novoPagamentoData.dataPagamento = new Date(novoPagamentoData.dataPagamento).toISOString().split('T')[0];
            }

            this.http.post('/api/pagamentos', novoPagamentoData).subscribe({
                next: (response) => {
                    this.notificationService.success('Pagamento lançado com sucesso!');
                    this.fetchPagamentos();
                    this.fecharFormularioPagamento();
                },
                error: (error) => {
                    this.notificationService.error('Erro ao lançar pagamento.');
                    console.error('Erro ao lançar pagamento:', error);
                }
            });
        } else {
            this.notificationService.warning('Por favor, preencha todos os campos obrigatórios corretamente.');
            this.pagamentoForm.markAllAsTouched();
        }
    }

    confirmDarBaixa(pagamento: any): void {
        this.confirmationModalService.confirm(`Confirmar baixa do pagamento de ${pagamento.associado?.nome} no valor de R$ ${pagamento.valor}?`).subscribe(confirmed => {
            if (confirmed) {
                this.darBaixa(pagamento);
            }
        });
    }

    darBaixa(pagamento: any): void {
        const pagamentoAtualizado = {
            ...pagamento,
            dataPagamento: new Date().toISOString().split('T')[0], // Data atual para baixa
            formaPagamento: 'Dinheiro' // Pode ser um modal para escolher a forma
        };

        // O backend espera o associado.id no DTO, não o objeto associado completo
        const pagamentoParaBackend = {
            associadoId: pagamentoAtualizado.associado.id,
            valor: pagamentoAtualizado.valor,
            dataPagamento: pagamentoAtualizado.dataPagamento,
            tipoPagamento: pagamentoAtualizado.tipoPagamento,
            formaPagamento: pagamentoAtualizado.formaPagamento
        };

        this.http.put(`/api/pagamentos/${pagamento.id}`, pagamentoParaBackend).subscribe({
            next: (response) => {
                this.notificationService.success('Pagamento baixado com sucesso!');
                this.fetchPagamentos();
            },
            error: (error) => {
                this.notificationService.error('Erro ao dar baixa no pagamento.');
                console.error('Erro ao dar baixa no pagamento:', error);
            }
        });
    }
}
