import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MensagemService {
  private apiUrl = '/api/mensagens';

  constructor(private http: HttpClient) { }

  enviarMensagem(mensagem: any): Observable<any> {
    return this.http.post(this.apiUrl, mensagem);
  }

  listarMensagens(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }
}
