import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ConfirmationModalService {
  private confirmationSubject = new Subject<boolean>();
  private messageSubject = new Subject<string>();
  private showSubject = new Subject<boolean>();

  constructor() { }

  /**
   * Exibe o modal de confirmação.
   * @param message A mensagem a ser exibida no modal.
   * @returns Um Observable que emite `true` se confirmado, `false` se cancelado.
   */
  confirm(message: string): Observable<boolean> {
    this.messageSubject.next(message);
    this.showSubject.next(true);
    return this.confirmationSubject.asObservable();
  }

  getMessage(): Observable<string> {
    return this.messageSubject.asObservable();
  }

  getShowStatus(): Observable<boolean> {
    return this.showSubject.asObservable();
  }

  resolve(confirmed: boolean): void {
    this.confirmationSubject.next(confirmed);
    this.showSubject.next(false); // Esconde o modal após a resolução
  }
}
