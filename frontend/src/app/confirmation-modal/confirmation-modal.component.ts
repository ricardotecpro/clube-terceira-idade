import { Component, OnInit, OnDestroy } from '@angular/core';
import { ConfirmationModalService } from './confirmation-modal.service';
import { Subscription } from 'rxjs';
import { NgIf, AsyncPipe } from '@angular/common';

@Component({
  selector: 'app-confirmation-modal',
  templateUrl: './confirmation-modal.component.html',
  styleUrls: ['./confirmation-modal.component.css'],
  standalone: false // Para ser usado em NgModule
})
export class ConfirmationModalComponent implements OnInit, OnDestroy {
  message: string = '';
  showModal: boolean = false;
  private subscription: Subscription = new Subscription();

  constructor(private modalService: ConfirmationModalService) { }

  ngOnInit(): void {
    this.subscription.add(this.modalService.getMessage().subscribe(message => {
      this.message = message;
    }));
    this.subscription.add(this.modalService.getShowStatus().subscribe(show => {
      this.showModal = show;
    }));
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  confirm(): void {
    this.modalService.resolve(true);
  }

  cancel(): void {
    this.modalService.resolve(false);
  }
}
