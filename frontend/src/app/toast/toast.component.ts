import { Component, OnInit, OnDestroy } from '@angular/core';
import { NotificationService, Notification } from '../notification.service';
import { Subscription } from 'rxjs';
import { CommonModule } from '@angular/common'; // Importar CommonModule

// Importar módulos Angular Material
import { MatSnackBarModule } from '@angular/material/snack-bar'; // Se for usar MatSnackBar
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card'; // Para o toast customizado

@Component({
  selector: 'app-toast',
  templateUrl: './toast.component.html',
  styleUrls: ['./toast.component.css'],
  standalone: true, // AGORA É STANDALONE
  imports: [
    CommonModule,
    MatSnackBarModule, // Se for usar MatSnackBar
    MatIconModule,
    MatButtonModule,
    MatCardModule
  ]
})
export class ToastComponent implements OnInit, OnDestroy {
  currentNotification: Notification | null = null;
  private notificationSubscription: Subscription = new Subscription();
  private timer: any;

  constructor(private notificationService: NotificationService) { }

  ngOnInit(): void {
    this.notificationSubscription = this.notificationService.getNotification().subscribe(notification => {
      this.currentNotification = notification;
      if (this.timer) {
        clearTimeout(this.timer);
      }
      this.timer = setTimeout(() => {
        this.currentNotification = null;
      }, notification.duration || 3000);
    });
  }

  ngOnDestroy(): void {
    this.notificationSubscription.unsubscribe();
    if (this.timer) {
      clearTimeout(this.timer);
    }
  }

  closeToast(): void {
    this.currentNotification = null;
    if (this.timer) {
      clearTimeout(this.timer);
    }
  }
}
