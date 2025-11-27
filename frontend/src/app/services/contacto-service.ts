import {inject, Injectable} from '@angular/core';
import {environment} from '../../environments/environmet';
import {HttpClient} from '@angular/common/http';
import {Observable, Subject} from 'rxjs';
import {ContactoMensaje, Psicologo} from '../model/interfaces';

@Injectable({
  providedIn: 'root'
})

export class ContactoService {
  private url = environment.apiURL;
  private http: HttpClient = inject(HttpClient);

  constructor() { }

  registrar(contactoMensaje: ContactoMensaje): Observable<any>{
    return this.http.post(this.url + "/contacto/registrar", contactoMensaje);
  }
  listarContactoMensajes(): Observable<any>{
    return this.http.get<ContactoMensaje[]>(this.url + "/contacto/listarContactoMensajes");
  }
}
