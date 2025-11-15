import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CidadesService {
  private apiUrl = '/api/cidades';

  constructor(private http: HttpClient) { }

  getCidades(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  getCidade(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  createCidade(cidade: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, cidade);
  }

  updateCidade(id: number, cidade: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, cidade);
  }

  deleteCidade(id: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${id}`);
  }
}
