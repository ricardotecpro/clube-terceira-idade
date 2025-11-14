import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AssociadosService {
  private apiUrl = '/api/associados';

  constructor(private http: HttpClient) { }

  getAssociados(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  getAssociadoById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  createAssociado(associado: any): Observable<any> {
    return this.http.post(this.apiUrl, associado);
  }

  updateAssociado(id: number, associado: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, associado);
  }

  deleteAssociado(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}
