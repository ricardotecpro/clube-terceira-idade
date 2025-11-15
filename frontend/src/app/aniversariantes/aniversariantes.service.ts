import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AniversariantesService {
  private apiUrl = '/api/aniversariantes';

  constructor(private http: HttpClient) { }

  /**
   * Busca aniversariantes com base no período.
   * @param periodo pode ser 'dia', 'semana', ou 'mes'.
   */
  getAniversariantes(periodo: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}?periodo=${periodo}`);
  }
}
