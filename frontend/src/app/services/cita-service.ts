import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environmet';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cita } from '../model/interfaces';

@Injectable({
  providedIn: 'root',
})
export class CitaService {
  private url = environment.apiURL;
  private http: HttpClient = inject(HttpClient);

  constructor() {}

  registrar(cita: Cita): Observable<any> {
    return this.http.post(this.url + '/cita/registrar', cita);
  }

  modificar(codigo: number, cita: Cita): Observable<any> {
    return this.http.put(`${this.url}/cita/modificar/${codigo}`, cita);
  }

  listarPorPaciente(paciente: string): Observable<Cita[]> {
    return this.http.get<Cita[]>(`${this.url}/cita/listarPorPaciente/${paciente}`);
  }

  listarPorPsicologo(psicologo: string): Observable<Cita[]> {
    return this.http.get<Cita[]>(`${this.url}/cita/listarPorPsicologo/${psicologo}`);
  }

  listarPorEspecialidad(especialidad: string): Observable<Cita[]> {
    return this.http.get<Cita[]>(
      `${this.url}/cita/listarPorEspecialidad/${especialidad}`
    );
  }

  listarCitas(): Observable<Cita[]> {
    return this.http.get<Cita[]>(this.url + '/cita/listarCitas');
  }
}
