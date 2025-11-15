import { Component, OnInit } from '@angular/core';
import { DashboardService } from './dashboard.service';
import { NotificationService } from '../notification.service';
import { CommonModule } from '@angular/common'; // Importar CommonModule

// Importar módulos Angular Material
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.html',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatIconModule
  ]
})
export class DashboardComponent implements OnInit {
  metrics: any = {};

  constructor(
    private dashboardService: DashboardService,
    private notificationService: NotificationService
  ) { }

  ngOnInit(): void {
    this.loadMetrics();
  }

  loadMetrics(): void {
    this.dashboardService.getMetrics().subscribe({
      next: (data) => {
        this.metrics = data;
      },
      error: (error) => {
        this.notificationService.error('Erro ao carregar métricas do dashboard.');
        console.error('Erro ao carregar métricas:', error);
      }
    });
  }
}
