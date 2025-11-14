import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { tap, catchError, map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private isAuthenticated = false;

  constructor(private router: Router, private http: HttpClient) { }

  login(credentials: { username: string, password: string }): Observable<boolean> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/x-www-form-urlencoded' });
    const body = `username=${credentials.username}&password=${credentials.password}`;

    return this.http.post('/api/login', body, { headers, observe: 'response' }).pipe(
      map(response => {
        if (response.status === 200) {
          this.isAuthenticated = true;
          this.router.navigate(['/dashboard']);
          return true;
        }
        this.isAuthenticated = false;
        return false;
      }),
      catchError(error => {
        console.error('Login failed:', error);
        this.isAuthenticated = false;
        return of(false);
      })
    );
  }

  logout(): void {
    this.http.post('/api/logout', {}).subscribe(() => {
      this.isAuthenticated = false;
      this.router.navigate(['/login']);
    });
  }

  isLoggedIn(): boolean {
    // Em um cenário real, você verificaria a validade de um token aqui
    return this.isAuthenticated;
  }
}
