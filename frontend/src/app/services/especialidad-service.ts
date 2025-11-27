import {inject, Injectable} from '@angular/core';
import {environment} from '../../environments/environmet';
import {HttpClient} from '@angular/common/http';
import {Especialidad, Paciente} from '../model/interfaces';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class EspecialidadService {
  private url = environment.apiURL;
  private http: HttpClient = inject(HttpClient);

  constructor() { }

  registrar(especialidad: Especialidad): Observable<any>{
    return this.http.post(this.url + "/especialidad/registrar", especialidad);
  }
  eliminar(nombre: string): Observable<any>{
    return this.http.delete(`${this.url}/especialidad/eliminar/${nombre}`);
  }
  listarPorCategoria(categoria: string): Observable<any>{
    console.log(this.url + "/especialidad/listarPorCategoria/" + categoria)
    return this.http.get<Especialidad>(`${this.url}/especialidad/listarPorCategoria/${categoria}`);
  }
  listarEspecialidadesActivas(): Observable<any>{
    return this.http.get<Especialidad[]>(this.url + "/especialidad/listarEspecialidadesActivas");
  }
  listarEspecialidades(): Observable<any>{
    return this.http.get<Especialidad[]>(this.url + "/especialidad/listarEspecialidades");
  }
}
