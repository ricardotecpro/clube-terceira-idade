import { Component } from '@angular/core';
import { LoadingService } from '../loading.service';
import { AsyncPipe, NgIf } from '@angular/common';

@Component({
  selector: 'app-loading-spinner',
  templateUrl: './loading-spinner.component.html',
  styleUrls: ['./loading-spinner.component.css'],
  standalone: false // Para ser usado em NgModule
})
export class LoadingSpinnerComponent {
  constructor(public loadingService: LoadingService) { }
}
