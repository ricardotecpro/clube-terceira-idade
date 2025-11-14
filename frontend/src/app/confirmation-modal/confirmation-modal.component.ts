import { Component, OnInit, OnDestroy } from '@angular/core';
import { ConfirmationModalService } from './confirmation-modal.service';
import { Subscription } from 'rxjs';
import { CommonModule } from '@angular/common'; // Importar CommonModule

// Importar módulos Angular Material
import { MatDialogModule } from '@angular/material/dialog'; // Se for usar MatDialog
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card'; // Para o modal customizado

@Component({
  selector: 'app-confirmation-modal',
  templateUrl: './confirmation-modal.component.html',
  styleUrls: ['./confirmation-modal.component.css'],
  standalone: true, // AGORA É STANDALONE
  imports: [
    CommonModule,
    MatDialogModule, // Se for usar MatDialog
    MatButtonModule,
    MatCardModule
  ]
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
