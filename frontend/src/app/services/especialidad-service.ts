import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environmet';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';

export interface Especialidad {
  idEspecialidad?: number;
  nombre: string;
  categoria: string;
  activo?: boolean;
}

@Injectable({
  providedIn: 'root',
})
export class EspecialidadService {
  private url = environment.apiURL;
  private http: HttpClient = inject(HttpClient);
  private listaCambio = new Subject<Especialidad[]>();

  constructor() {}

  registrar(especialidad: Especialidad): Observable<any> {
    return this.http.post(this.url + '/especialidad/registrar', especialidad);
  }

  eliminar(nombre: string): Observable<any> {
    return this.http.delete(`${this.url}/especialidad/eliminar/${nombre}`);
  }

  listarPorCategoria(categoria: string): Observable<Especialidad[]> {
    return this.http.get<Especialidad[]>(
      `${this.url}/especialidad/listarPorCategoria/${categoria}`
    );
  }

  listarEspecialidadesActivas(): Observable<Especialidad[]> {
    return this.http.get<Especialidad[]>(
      `${this.url}/especialidad/listarEspecialidadesActivas`
    );
  }

  listarTodo(): Observable<Especialidad[]> {
    return this.http.get<Especialidad[]>(
      `${this.url}/especialidad/listarTodo`
    );
  }

  setList(listaNueva: Especialidad[]) {
    this.listaCambio.next(listaNueva);
  }

  getListaCambio(): Observable<Especialidad[]> {
    return this.listaCambio.asObservable();
  }

  actualizarLista(): void {
    this.listarTodo().subscribe({
      next: (data) => this.setList(data),
      error: (err) => console.error('Error actualizando lista', err),
    });
  }
}
