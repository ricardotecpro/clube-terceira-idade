import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private apiUrl = '/api/admin';

  constructor(private http: HttpClient) { }

  performBackup(): Observable<string> {
    return this.http.post(`${this.apiUrl}/backup`, {}, { responseType: 'text' });
  }

  performRestore(backupFileName: string): Observable<string> {
    return this.http.post(`${this.apiUrl}/restore?backupFileName=${backupFileName}`, {}, { responseType: 'text' });
  }
}
