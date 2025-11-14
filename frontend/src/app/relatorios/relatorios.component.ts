import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormBuilder, FormGroup, Validators } from '@angular/forms'; // Importar
import { NotificationService } from '../notification.service'; // Importar

@Component({
    selector: 'app-relatorios',
    templateUrl: './relatorios.html',
    styleUrl: './relatorios.css',
    standalone: false
})
export class RelatoriosComponent implements OnInit {
    relatorioForm!: FormGroup; // Usar FormGroup

    constructor(
        private http: HttpClient,
        private fb: FormBuilder, // Injetar FormBuilder
        private notificationService: NotificationService // Injetar
    ) { }

    ngOnInit(): void {
        this.relatorioForm = this.fb.group({
            filtroBairro: [''],
            filtroEscolaridade: [''],
            idadeMin: [null, [Validators.min(0), Validators.max(150)]], // Novo campo
            idadeMax: [null, [Validators.min(0), Validators.max(150)]]  // Novo campo
        });
    }

    gerarPdfAssociados(): void {
        const { filtroBairro, filtroEscolaridade, idadeMin, idadeMax } = this.relatorioForm.value;
        let params = new URLSearchParams();
        if (filtroBairro) {
            params.append('bairro', filtroBairro);
        }
        if (filtroEscolaridade) {
            params.append('escolaridade', filtroEscolaridade);
        }
        if (idadeMin !== null) {
            params.append('idadeMin', idadeMin.toString());
        }
        if (idadeMax !== null) {
            params.append('idadeMax', idadeMax.toString());
        }

        window.open(`/api/relatorios/associados/pdf?${params.toString()}`, '_blank');
        this.notificationService.info('Gerando relatório PDF...');
    }

    gerarExcelAssociados(): void {
        const { filtroBairro, filtroEscolaridade, idadeMin, idadeMax } = this.relatorioForm.value;
        let params = new URLSearchParams();
        if (filtroBairro) {
            params.append('bairro', filtroBairro);
        }
        if (filtroEscolaridade) {
            params.append('escolaridade', filtroEscolaridade);
        }
        if (idadeMin !== null) {
            params.append('idadeMin', idadeMin.toString());
        }
        if (idadeMax !== null) {
            params.append('idadeMax', idadeMax.toString());
        }

        window.open(`/api/relatorios/associados/excel?${params.toString()}`, '_blank');
        this.notificationService.info('Gerando relatório Excel...');
    }
}
