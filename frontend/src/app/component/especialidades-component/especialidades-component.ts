import { Component, inject, signal, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { RoutingService } from '../../services/routing-service';
import { EspecialidadService, Especialidad } from '../../services/especialidad-service';

@Component({
  selector: 'app-especialidades',
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './especialidades-component.html',
  styleUrl: './especialidades-component.css',
})
export class EspecialidadesComponent implements OnInit {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private roleRouting = inject(RoutingService);
  private especialidadService = inject(EspecialidadService);

  especialidades = signal<Especialidad[]>([]);
  especialidadesFiltradas = signal<Especialidad[]>([]);
  isLoading = signal<boolean>(false);
  isLoadingData = signal<boolean>(true);
  errorMessage = signal<string>('');
  successMessage = signal<string>('');
  showModal = signal<boolean>(false);
  categoriaFiltro = signal<string>('todas');

  especialidadForm = this.fb.group({
    nombre: ['', [Validators.required, Validators.minLength(3)]],
    categoria: ['', Validators.required],
  });

  categorias = [
    'Ansiedad y Estrés',
    'Depresión',
    'Trastornos de Personalidad',
    'Adicciones',
    'Relaciones y Familia',
    'Infantil y Adolescente',
    'Trauma y PTSD',
    'Trastornos Alimentarios',
    'Otros'
  ];

  ngOnInit(): void {
    this.cargarEspecialidades();
  }

  cargarEspecialidades(): void {
    this.isLoadingData.set(true);
    this.especialidadService.listarTodo().subscribe({
      next: (data) => {
        this.especialidades.set(data);
        this.aplicarFiltro();
        this.isLoadingData.set(false);
      },
      error: (error) => {
        console.error('Error al cargar especialidades:', error);
        this.errorMessage.set('Error al cargar las especialidades');
        this.isLoadingData.set(false);
      },
    });
  }

  filtrarPorCategoria(categoria: string): void {
    this.categoriaFiltro.set(categoria);
    this.aplicarFiltro();
  }

  aplicarFiltro(): void {
    const cat = this.categoriaFiltro();
    if (cat === 'todas') {
      this.especialidadesFiltradas.set(this.especialidades());
    } else {
      this.especialidadesFiltradas.set(
        this.especialidades().filter((e) => e.categoria === cat)
      );
    }
  }

  abrirModal(): void {
    this.showModal.set(true);
    this.especialidadForm.reset();
    this.errorMessage.set('');
    this.successMessage.set('');
  }

  cerrarModal(): void {
    this.showModal.set(false);
    this.especialidadForm.reset();
  }

  onSubmit(): void {
    if (this.especialidadForm.invalid) {
      this.errorMessage.set('Por favor, completa todos los campos');
      return;
    }

    this.isLoading.set(true);
    this.errorMessage.set('');

    const especialidad: Especialidad = {
      nombre: this.especialidadForm.value.nombre!,
      categoria: this.especialidadForm.value.categoria!,
      activo: true,
    };

    this.especialidadService.registrar(especialidad).subscribe({
      next: () => {
        this.successMessage.set('Especialidad registrada exitosamente');
        this.isLoading.set(false);
        this.cerrarModal();
        this.cargarEspecialidades();
      },
      error: (error) => {
        console.error('Error al registrar:', error);
        this.isLoading.set(false);
        this.errorMessage.set(
          error.error?.message || 'Error al registrar la especialidad'
        );
      },
    });
  }

  eliminarEspecialidad(nombre: string): void {
    if (!confirm(`¿Estás seguro de eliminar la especialidad "${nombre}"?`)) {
      return;
    }

    this.especialidadService.eliminar(nombre).subscribe({
      next: () => {
        this.successMessage.set('Especialidad eliminada exitosamente');
        this.cargarEspecialidades();
        setTimeout(() => this.successMessage.set(''), 3000);
      },
      error: (error) => {
        console.error('Error al eliminar:', error);
        this.errorMessage.set(
          error.error?.message || 'Error al eliminar la especialidad'
        );
        setTimeout(() => this.errorMessage.set(''), 5000);
      },
    });
  }

  logout(): void {
    this.authService.logout();
  }

  goHome() {
    this.roleRouting.goHome();
  }

  goPerfil() {
    this.roleRouting.goPerfil();
  }

  getFieldError(fieldName: string): string | null {
    const field = this.especialidadForm.get(fieldName);

    if (!field?.touched) {
      return null;
    }

    if (field.hasError('required')) {
      return 'Este campo es requerido';
    }
    if (field.hasError('minlength')) {
      const minLength = field.errors?.['minlength'].requiredLength;
      return `Mínimo ${minLength} caracteres`;
    }
    return null;
  }
}
