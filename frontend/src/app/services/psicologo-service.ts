import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environmet';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import {Paciente, Psicologo} from '../model/interfaces';

@Injectable({
  providedIn: 'root',
})
export class PsicologoService {
  private url = environment.apiURL;
  private http: HttpClient = inject(HttpClient);
  private listaCambio = new Subject<Psicologo[]>();

  constructor() { }

  registrar(psicologo: Psicologo): Observable<any>{
    return this.http.post(this.url + "/psicologo/registrar", psicologo);
  }
  modificar(psicologo: Psicologo): Observable<any>{
    return this.http.put(this.url + "/psicologo/modificar", psicologo);
  }

  listarPorDni(dni: string): Observable<any>{
    console.log(this.url + "/psicologo/listarPorDni/" + dni)
    return this.http.get<Paciente>(`${this.url}/psicologo/listarPorDni/${dni}`);
  }

  listarPsicologosActivos(): Observable<any>{
    return this.http.get<Psicologo[]>(this.url + "/psicologo/listarPsicologosActivos");
  }
  listarTodo(): Observable<any>{
    return this.http.get<Psicologo[]>(this.url + "/psicologo/listarTodo");
  }
  setList(listaNueva : Psicologo[]){
    this.listaCambio.next(listaNueva);//enviar la nueva lista a los suscriptores
  }
  getListaCambio(): Observable<Psicologo[]>{
    return this.listaCambio.asObservable();
  }
  actualizarLista(): void {
    this.listarTodo().subscribe({
      next: (data) => this.setList(data),   //envia la nueva lista a los suscriptores
      error: (err) => console.error('Error actualizando lista', err)
    });
  }
}
