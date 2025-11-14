import { Component, OnInit } from '@angular/core';
import { AdminService } from './admin.service';
import { NotificationService } from '../notification.service';
import { ConfirmationModalService } from '../confirmation-modal/confirmation-modal.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css'],
  standalone: false
})
export class AdminComponent implements OnInit {
  backupFileName: string = ''; // Para o nome do arquivo de restore

  constructor(
    private adminService: AdminService,
    private notificationService: NotificationService,
    private confirmationModalService: ConfirmationModalService
  ) { }

  ngOnInit(): void {
  }

  confirmBackup(): void {
    this.confirmationModalService.confirm('Tem certeza que deseja realizar um backup agora?').subscribe(confirmed => {
      if (confirmed) {
        this.performBackup();
      }
    });
  }

  performBackup(): void {
    this.adminService.performBackup().subscribe({
      next: (response) => {
        this.notificationService.success(response);
      },
      error: (error) => {
        this.notificationService.error('Erro ao realizar backup.');
        console.error('Erro ao realizar backup:', error);
      }
    });
  }

  confirmRestore(): void {
    if (!this.backupFileName) {
      this.notificationService.warning('Por favor, informe o nome do arquivo de backup para restaurar.');
      return;
    }
    this.confirmationModalService.confirm(`ATENÇÃO: A restauração de dados irá sobrescrever o banco de dados atual. Tem certeza que deseja restaurar a partir de "${this.backupFileName}"?`).subscribe(confirmed => {
      if (confirmed) {
        this.performRestore();
      }
    });
  }

  performRestore(): void {
    this.adminService.performRestore(this.backupFileName).subscribe({
      next: (response) => {
        this.notificationService.success(response);
      },
      error: (error) => {
        this.notificationService.error('Erro ao realizar restauração.');
        console.error('Erro ao realizar restauração:', error);
      }
    });
  }
}
