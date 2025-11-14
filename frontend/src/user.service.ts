import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

const API_URL = 'http://localhost:8080/admin/users';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  listarUsuarios(): Observable<any> {
    // Esta requisição irá para http://localhost:8080/admin/users
    return this.http.get(API_URL);
  }
  salvarUsuario(user: any): Observable<any> {
    return this.http.post(`${API_URL}/salvar`, user);
  }
}
