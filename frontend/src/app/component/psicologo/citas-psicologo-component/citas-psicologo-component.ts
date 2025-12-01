import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { RoutingService } from '../../../services/routing-service';
import { CitaService, Cita } from '../../../services/cita-service';
import { PsicologoService } from '../../../services/psicologo-service';

@Component({
  selector: 'app-citas-psicologo',
  imports: [CommonModule, RouterModule],
  templateUrl: './citas-psicologo-component.html',
  styleUrl: './citas-psicologo-component.css',
})
export class CitasPsicologoComponent implements OnInit {
  private authService = inject(AuthService);
  private roleRouting = inject(RoutingService);
  private citaService = inject(CitaService);
  private psicologoService = inject(PsicologoService);

  citas = signal<Cita[]>([]);
  isLoadingData = signal<boolean>(true);
  errorMessage = signal<string>('');
  successMessage = signal<string>('');
  psicologoId = signal<number>(0);
  filtroEstado = signal<string>('todas');
  showDetailModal = signal<boolean>(false);
  citaSeleccionada = signal<Cita | null>(null);

  ngOnInit(): void {
    this.cargarDatosIniciales();
  }

  cargarDatosIniciales(): void {
    const userDni = localStorage.getItem('user_dni');

    if (!userDni) {
      this.errorMessage.set('No se encontró información del usuario');
      return;
    }

    this.psicologoService.listarPorDni(userDni).subscribe({
      next: (psicologo) => {
        this.psicologoId.set(psicologo.idPsicologo);
        this.cargarCitas();
      },
      error: (error) => {
        console.error('Error al obtener datos del psicólogo:', error);
        this.errorMessage.set('Error al cargar información del psicólogo');
      },
    });
  }

  cargarCitas(): void {
    this.isLoadingData.set(true);
    const psicologoId = this.psicologoId();

    if (psicologoId === 0) {
      this.isLoadingData.set(false);
      return;
    }

    this.citaService.listarPorPsicologo(psicologoId).subscribe({
      next: (data: Cita[]) => {
        const citasOrdenadas: Cita[] = data.sort((a: Cita, b: Cita) => {
          const fechaA = new Date(`${a.fecha}T${a.hora}`);
          const fechaB = new Date(`${b.fecha}T${b.hora}`);
          return fechaB.getTime() - fechaA.getTime();
        });
        this.citas.set(citasOrdenadas);
        this.isLoadingData.set(false);
      },
      error: (error) => {
        console.error('Error al cargar citas:', error);
        this.errorMessage.set('Error al cargar las citas');
        this.isLoadingData.set(false);
      },
    });
  }

  get citasFiltradas(): Cita[] {
    const filtro = this.filtroEstado();
    if (filtro === 'todas') {
      return this.citas();
    }
    return this.citas().filter((c: Cita) => c.estado === filtro);
  }

  verDetalle(cita: Cita): void {
    this.citaSeleccionada.set(cita);
    this.showDetailModal.set(true);
  }

  cerrarModal(): void {
    this.showDetailModal.set(false);
    this.citaSeleccionada.set(null);
  }

  confirmarCita(citaId: number | undefined): void {
    if (!citaId) return;

    this.citaService.confirmar(citaId).subscribe({
      next: () => {
        this.successMessage.set('Cita confirmada exitosamente');
        this.cargarCitas();
        this.cerrarModal();
        setTimeout(() => this.successMessage.set(''), 5000);
      },
      error: (error) => {
        console.error('Error al confirmar:', error);
        this.errorMessage.set(error.error?.message || 'Error al confirmar la cita');
      },
    });
  }

  completarCita(citaId: number | undefined): void {
    if (!citaId) return;

    this.citaService.completar(citaId).subscribe({
      next: () => {
        this.successMessage.set('Cita completada exitosamente');
        this.cargarCitas();
        this.cerrarModal();
        setTimeout(() => this.successMessage.set(''), 5000);
      },
      error: (error) => {
        console.error('Error al completar:', error);
        this.errorMessage.set(error.error?.message || 'Error al completar la cita');
      },
    });
  }

  getEstadoClass(estado: string | undefined): string {
    switch (estado) {
      case 'Pendiente': return 'estado-pendiente';
      case 'Confirmada': return 'estado-confirmada';
      case 'Completada': return 'estado-completada';
      case 'Cancelada': return 'estado-cancelada';
      default: return '';
    }
  }

  formatearFecha(fecha: string): string {
    const date = new Date(fecha);
    return date.toLocaleDateString('es-PE', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    });
  }

  formatearHora(hora: string): string {
    return hora.substring(0, 5);
  }

  logout(): void {
    this.authService.logout();
  }

  goHome(): void {
    this.roleRouting.goHome();
  }

  goPerfil(): void {
    this.roleRouting.goPerfil();
  }

  get citasHoy(): Cita[] {
    const hoy = new Date().toISOString().split('T')[0];
    return this.citas().filter((c: Cita) => c.fecha === hoy);
  }

  get citasPendientes(): Cita[] {
    return this.citas().filter((c: Cita) => c.estado === 'Pendiente');
  }

  get citasConfirmadas(): Cita[] {
    return this.citas().filter((c: Cita) => c.estado === 'Confirmada');
  }
}
