import { Component, OnInit } from '@angular/core';
import { DashboardService } from './dashboard.service';
import { NotificationService } from '../notification.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
  standalone: false
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
