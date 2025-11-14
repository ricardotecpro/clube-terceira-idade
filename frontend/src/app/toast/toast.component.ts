import { Component, OnInit, OnDestroy } from '@angular/core';
import { NotificationService, Notification } from '../notification.service';
import { Subscription } from 'rxjs';
import { NgIf, NgClass } from '@angular/common';

@Component({
  selector: 'app-toast',
  templateUrl: './toast.component.html',
  styleUrls: ['./toast.component.css'],
  standalone: false // Para ser usado em NgModule
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
