import { inject, Injectable} from '@angular/core';
import { environment } from '../../environments/environmet';
import { HttpClient } from '@angular/common/http';
import { AuthResponse } from '../model/interfaces';
import { map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  private url = environment.apiURL;
  private http: HttpClient = inject(HttpClient);
  constructor() { }

  login(requestDTO: AuthResponse): Observable<any> {
    console.log("Enviando:", requestDTO)
    return this.http.post(this.url + "/auth/login", requestDTO,
      {observe: 'response'}).pipe(map((response) => {
        const body = response.body;
        console.log("Body:", body)
        const headers = response.headers;
        const bearerToken = headers.get('Authorization')!;
        const token = bearerToken.replace('Bearer ', '');
        console.log("Authorization:", bearerToken)
        localStorage.setItem('token', token);
        return body;
      }
    ));
  }
}
