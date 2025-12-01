import { inject, Injectable} from '@angular/core';
import { environment } from '../../environments/environmet';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { Paciente } from '../model/interfaces';

@Injectable({
  providedIn: 'root',
})
export class PacienteService {
  private url = environment.apiURL;
  private http: HttpClient = inject(HttpClient);
  private listaCambio = new Subject<Paciente[]>();

  constructor() { }

  registrar(paciente: Paciente): Observable<any>{
    return this.http.post(this.url + "/paciente/registrar", paciente);
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
  listarTodo(): Observable<any>{
    return this.http.get<Paciente[]>(this.url + "/paciente/listarTodo");
  }
  setList(listaNueva : Paciente[]){
    this.listaCambio.next(listaNueva);//enviar la nueva lista a los suscriptores
  }
  getListaCambio(): Observable<Paciente[]>{
    return this.listaCambio.asObservable();
  }
  actualizarLista(): void {
    this.listarTodo().subscribe({
      next: (data) => this.setList(data),   //envia la nueva lista a los suscriptores
      error: (err) => console.error('Error actualizando lista', err)
    });
  }
}
