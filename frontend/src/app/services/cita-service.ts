import {inject, Injectable} from '@angular/core';
import {environment} from '../../environments/environmet';
import {HttpClient} from '@angular/common/http';
import {Observable, Subject} from 'rxjs';
import {Cita} from '../model/interfaces';

@Injectable({
  providedIn: 'root',
})
export class CitaService {
  private url = environment.apiURL;
  private http: HttpClient = inject(HttpClient);

  constructor() { }

  registrar(cita: Cita): Observable<any>{
    return this.http.post(this.url + "/cita/registrar", cita);
  }
  modificar(cita: Cita): Observable<any>{
    return this.http.put(this.url + "/cita/modificar", cita);
  }

  listarPorPaciente(paciente: string): Observable<any>{
    console.log(this.url + "/cita/listarPorPaciente/" + paciente)
    return this.http.get<Cita>(`${this.url}/cita/listarPorPaciente/${paciente}`);
  }

  listarPorPsicologo(psicologo: string): Observable<any>{
    console.log(this.url + "/cita/listarPorPsicologo/" + psicologo)
    return this.http.get<Cita>(`${this.url}/cita/listarPorPsicologo/${psicologo}`);
  }

  listarPorEspecialidad(especialidad: string): Observable<any>{
    console.log(this.url + "/cita/listarPorEspecialidad/" + especialidad)
    return this.http.get<Cita>(`${this.url}/cita/listarPorEspecialidad/${especialidad}`);
  }

  listarCitas(): Observable<any>{
    return this.http.get<Cita[]>(this.url + "/psicologo/listarCitas");
  }
}
