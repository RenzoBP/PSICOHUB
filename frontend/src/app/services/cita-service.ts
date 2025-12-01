import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environmet';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';

// ✅ Interfaces exportadas ANTES del servicio
export interface Cita {
  idCita?: number;
  pacienteId: number;
  pacienteNombre?: string;
  pacienteApellido?: string;
  pacienteEmail?: string;
  psicologoId: number;
  psicologoNombre?: string;
  psicologoApellido?: string;
  psicologoEmail?: string;
  especialidadId: number;
  especialidadNombre?: string;
  especialidadCategoria?: string;
  fecha: string;
  hora: string;
  modalidad: string;
  estado?: string;
  motivoConsulta?: string;
  motivoCancelacion?: string;
  enlaceVirtual?: string;
  activo?: boolean;
}

export interface CitaRequest {
  pacienteId: number;
  psicologoId: number;
  especialidadId: number;
  fecha: string;
  hora: string;
  modalidad: string;
  motivoConsulta: string;
}

@Injectable({
  providedIn: 'root',
})
export class CitaService {
  private url = environment.apiURL;
  private http = inject(HttpClient);
  private listaCambio = new Subject<Cita[]>();

  registrar(cita: CitaRequest): Observable<Cita> {
    return this.http.post<Cita>(`${this.url}/citas/registrar`, cita);
  }

  modificar(id: number, cita: CitaRequest): Observable<Cita> {
    return this.http.put<Cita>(`${this.url}/citas/modificar/${id}`, cita);
  }

  cancelar(id: number, motivoCancelacion: string): Observable<any> {
    return this.http.put(`${this.url}/citas/cancelar/${id}`, { motivoCancelacion });
  }

  confirmar(id: number): Observable<any> {
    return this.http.put(`${this.url}/citas/confirmar/${id}`, {});
  }

  completar(id: number): Observable<any> {
    return this.http.put(`${this.url}/citas/completar/${id}`, {});
  }

  listarPorId(id: number): Observable<Cita> {
    return this.http.get<Cita>(`${this.url}/citas/listar/${id}`);
  }

  listarPorPaciente(pacienteId: number): Observable<Cita[]> {
    return this.http.get<Cita[]>(`${this.url}/citas/paciente/${pacienteId}`);
  }

  listarPorPsicologo(psicologoId: number): Observable<Cita[]> {
    return this.http.get<Cita[]>(`${this.url}/citas/psicologo/${psicologoId}`);
  }

  listarPorEstado(estado: string): Observable<Cita[]> {
    return this.http.get<Cita[]>(`${this.url}/citas/estado/${estado}`);
  }

  listarTodo(): Observable<Cita[]> {
    return this.http.get<Cita[]>(`${this.url}/citas/listarTodo`);
  }

  setList(listaNueva: Cita[]): void {
    this.listaCambio.next(listaNueva);
  }

  getListaCambio(): Observable<Cita[]> {
    return this.listaCambio.asObservable();
  }

  actualizarLista(): void {
    this.listarTodo().subscribe({
      next: (data) => this.setList(data),
      error: (err) => console.error('Error actualizando lista', err),
    });
  }
}
