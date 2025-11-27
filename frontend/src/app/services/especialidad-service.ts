import {inject, Injectable} from '@angular/core';
import {environment} from '../../environments/environmet';
import {HttpClient} from '@angular/common/http';
import {Paciente} from '../model/interfaces';
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
  modificar(paciente: Paciente): Observable<any>{
    return this.http.put(this.url + "/paciente/modificar", paciente);
  }
  listarPorDni(dni: string): Observable<any>{
    console.log(this.url + "/paciente/listarPorDni/" + dni)
    return this.http.get<Paciente>(`${this.url}/paciente/listarPorDni/${dni}`);
  }
  listarPacientesActivos(): Observable<any>{
    return this.http.get<Paciente[]>(this.url + "/paciente/listarPacientesActivos");
  }
  listarPacientes(): Observable<any>{
    return this.http.get<Paciente[]>(this.url + "/paciente/listarPacientes");
  }
}
