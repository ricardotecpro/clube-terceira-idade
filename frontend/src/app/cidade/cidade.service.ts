import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CidadeService {
  private apiUrl = '/api/cidades';

  constructor(private http: HttpClient) { }

  buscarCidades(nome: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}?nome=${nome}`);
  }

  criarCidade(cidade: any): Observable<any> {
    return this.http.post(this.apiUrl, cidade);
  }
}
