import { Component, inject, signal, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { RoutingService } from '../../../services/routing-service';
import { CitaService, Cita, CitaRequest } from '../../../services/cita-service';
import { PsicologoService } from '../../../services/psicologo-service';
import { EspecialidadService, Especialidad } from '../../../services/especialidad-service';
import { PacienteService } from '../../../services/paciente-service';

@Component({
  selector: 'app-citas-paciente',
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './citas-paciente-component.html',
  styleUrl: './citas-paciente-component.css',
})
export class CitasPacienteComponent implements OnInit {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private roleRouting = inject(RoutingService);
  private citaService = inject(CitaService);
  private psicologoService = inject(PsicologoService);
  private especialidadService = inject(EspecialidadService);
  private pacienteService = inject(PacienteService);

  citas = signal<Cita[]>([]);
  psicologos = signal<any[]>([]);
  especialidades = signal<Especialidad[]>([]);
  isLoading = signal<boolean>(false);
  isLoadingData = signal<boolean>(true);
  errorMessage = signal<string>('');
  successMessage = signal<string>('');
  showModal = signal<boolean>(false);
  showCancelModal = signal<boolean>(false);
  citaSeleccionada = signal<Cita | null>(null);
  pacienteId = signal<number>(0);
  filtroEstado = signal<string>('todas');

  citaForm!: FormGroup;
  cancelacionForm!: FormGroup;

  horariosDisponibles = [
    '08:00', '09:00', '10:00', '11:00', '12:00',
    '14:00', '15:00', '16:00', '17:00', '18:00'
  ];

  ngOnInit(): void {
    this.initializeForms();
    this.cargarDatosIniciales();
  }

  initializeForms(): void {
    this.citaForm = this.fb.group({
      psicologoId: ['', Validators.required],
      especialidadId: ['', Validators.required],
      fecha: ['', Validators.required],
      hora: ['', Validators.required],
      motivoConsulta: ['', [Validators.required, Validators.minLength(10)]],
    });

    this.cancelacionForm = this.fb.group({
      motivoCancelacion: ['', [Validators.required, Validators.minLength(10)]],
    });
  }

  cargarDatosIniciales(): void {
    const userDni = localStorage.getItem('user_dni');

    if (!userDni) {
      this.errorMessage.set('No se encontró información del usuario');
      return;
    }

    this.pacienteService.listarPorDni(userDni).subscribe({
      next: (paciente) => {
        this.pacienteId.set(paciente.idPaciente);
        this.cargarCitas();
      },
      error: (error) => {
        console.error('Error al obtener datos del paciente:', error);
        this.errorMessage.set('Error al cargar información del paciente');
      },
    });

    this.psicologoService.listarPsicologosActivos().subscribe({
      next: (data) => {
        this.psicologos.set(data);
      },
      error: (error) => {
        console.error('Error al cargar psicólogos:', error);
      },
    });

    this.especialidadService.listarEspecialidadesActivas().subscribe({
      next: (data) => {
        this.especialidades.set(data);
      },
      error: (error) => {
        console.error('Error al cargar especialidades:', error);
      },
    });
  }

  cargarCitas(): void {
    this.isLoadingData.set(true);
    const pacienteId = this.pacienteId();

    if (pacienteId === 0) {
      this.isLoadingData.set(false);
      return;
    }

    this.citaService.listarPorPaciente(pacienteId).subscribe({
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

  abrirModal(): void {
    this.showModal.set(true);
    this.citaForm.reset();
    this.errorMessage.set('');
    this.successMessage.set('');
  }

  cerrarModal(): void {
    this.showModal.set(false);
    this.citaForm.reset();
  }

  abrirModalCancelacion(cita: Cita): void {
    this.citaSeleccionada.set(cita);
    this.showCancelModal.set(true);
    this.cancelacionForm.reset();
  }

  cerrarModalCancelacion(): void {
    this.showCancelModal.set(false);
    this.citaSeleccionada.set(null);
    this.cancelacionForm.reset();
  }

  onSubmit(): void {
    if (this.citaForm.invalid) {
      this.errorMessage.set('Por favor, completa todos los campos correctamente');
      return;
    }

    this.isLoading.set(true);
    this.errorMessage.set('');

    const citaRequest: CitaRequest = {
      pacienteId: this.pacienteId(),
      psicologoId: Number(this.citaForm.value.psicologoId!),
      especialidadId: Number(this.citaForm.value.especialidadId!),
      fecha: this.citaForm.value.fecha!,
      hora: this.citaForm.value.hora!,
      modalidad: 'Virtual', // ✅ Siempre Virtual
      motivoConsulta: this.citaForm.value.motivoConsulta!,
    };

    this.citaService.registrar(citaRequest).subscribe({
      next: () => {
        this.successMessage.set('Cita agendada exitosamente');
        this.isLoading.set(false);
        this.cerrarModal();
        this.cargarCitas();
        setTimeout(() => this.successMessage.set(''), 5000);
      },
      error: (error) => {
        console.error('Error al agendar:', error);
        this.isLoading.set(false);
        this.errorMessage.set(
          error.error?.message || 'Error al agendar la cita'
        );
      },
    });
  }

  confirmarCancelacion(): void {
    if (this.cancelacionForm.invalid) {
      return;
    }

    const cita = this.citaSeleccionada();
    if (!cita || !cita.idCita) return;

    this.citaService
      .cancelar(cita.idCita, this.cancelacionForm.value.motivoCancelacion!)
      .subscribe({
        next: () => {
          this.successMessage.set('Cita cancelada exitosamente');
          this.cerrarModalCancelacion();
          this.cargarCitas();
          setTimeout(() => this.successMessage.set(''), 5000);
        },
        error: (error) => {
          console.error('Error al cancelar:', error);
          this.errorMessage.set(error.error?.message || 'Error al cancelar la cita');
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

  getFieldError(formName: 'citaForm' | 'cancelacionForm', fieldName: string): string | null {
    const form: FormGroup = formName === 'citaForm' ? this.citaForm : this.cancelacionForm;
    const field = form.get(fieldName);

    if (!field?.touched) return null;

    if (field.hasError('required')) return 'Este campo es requerido';
    if (field.hasError('minlength')) {
      const minLength = field.errors?.['minlength'].requiredLength;
      return `Mínimo ${minLength} caracteres`;
    }
    return null;
  }
}
