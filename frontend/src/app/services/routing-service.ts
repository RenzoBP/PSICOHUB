import { Injectable, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class RoutingService {

  private authService = inject(AuthService);
  private router = inject(Router);

  // Devuelve "paciente" o "psicologo"
  getRolePath(): string {
    return this.authService.isPaciente() ? 'paciente' : 'psicologo';
  }

  // Navegar al home según rol
  goHome() {
    const role = this.getRolePath();
    this.router.navigate([`/home-${role}`]);
  }

  // Navegar al perfil según rol
  goPerfil() {
    const role = this.getRolePath();
    this.router.navigate([`/perfil-${role}`]);
  }

  // Opcional: contacto según rol
  goContact() {
    const role = this.getRolePath();
    this.router.navigate([`/home-${role}`], { fragment: 'contact' });
  }

  // Opcional: about según rol
  goAbout() {
    const role = this.getRolePath();
    this.router.navigate([`/home-${role}`], { fragment: 'about' });
  }
}
