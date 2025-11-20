import {Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { PacienteService } from '../../services/paciente-service';
import { PsicologoService } from '../../services/psicologo-service';
import { Paciente, Psicologo } from '../../model/interfaces';

@Component({
  selector: 'app-registro-component',
  standalone: true,
  imports: [RouterModule, ReactiveFormsModule, CommonModule],
  templateUrl: './registro-component.html',
  styleUrl: './registro-component.css',
})
export class RegistroComponent {
  registroForm: FormGroup;
  fb = inject(FormBuilder);
  pacienteService: PacienteService = inject(PacienteService);
  psicologoService: PsicologoService = inject(PsicologoService);
  router: Router =inject(Router);

  errorMessage = signal<string>('');
  successMessage = signal<string>('');
  isLoading = signal<boolean>(false);

  constructor() {
    console.log('RegistroComponent');
    this.registroForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2)]],
      apellido: ['', [Validators.required, Validators.minLength(2)]],
      dni: ['', [Validators.required, Validators.pattern(/^\d{8}$/)]],
      fechaNacimiento: ['', Validators.required],
      genero: ['', Validators.required],
      distrito: ['', Validators.required],
      direccion: ['', Validators.required],
      telefono: ['', [Validators.required, Validators.pattern(/^\d{9}$/)]],
      rol: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    })
  }

  onSubmit() {
    if (this.registroForm.invalid) {
      this.errorMessage.set('Por favor, completa todos los campos correctamente');
      Object.keys(this.registroForm.controls).forEach(key => {
        this.registroForm.get(key)?.markAsTouched();
      });
      return;
    }

    this.isLoading.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    const formData = this.registroForm.value;

    let request$;

    if (formData.rol === 'paciente'){
      const paciente: Paciente = new Paciente();
      paciente.idPaciente = 0;
      paciente.nombre = this.registroForm.value.nombre;
      paciente.apellido = this.registroForm.value.apellido;
      paciente.dni = this.registroForm.value.dni;
      paciente.fechaNacimiento = this.registroForm.value.fechaNacimiento;
      paciente.genero = this.registroForm.value.genero;
      paciente.distrito = this.registroForm.value.distrito;
      paciente.direccion = this.registroForm.value.direccion;
      paciente.telefono = this.registroForm.value.telefono;
      paciente.email = this.registroForm.value.email;
      paciente.password = this.registroForm.value.password;
      paciente.activo = true;

      console.log("Paciente validado para registrar:", paciente);
      request$ = this.pacienteService.registrar(paciente);

    }
    else if (formData.rol === 'psicologo'){
      const psicologo: Psicologo = new Psicologo();
      psicologo.idPsicologo = 0;
      psicologo.nombre = this.registroForm.value.nombre;
      psicologo.apellido = this.registroForm.value.apellido;
      psicologo.dni = this.registroForm.value.dni;
      psicologo.fechaNacimiento = this.registroForm.value.fechaNacimiento;
      psicologo.genero = this.registroForm.value.genero;
      psicologo.distrito = this.registroForm.value.distrito;
      psicologo.direccion = this.registroForm.value.direccion;
      psicologo.telefono = this.registroForm.value.telefono;
      psicologo.email = this.registroForm.value.email;
      psicologo.password = this.registroForm.value.password;
      psicologo.activo = true;

      console.log("Psicologo validado para registrar:", psicologo);
      request$ = this.psicologoService.registrar(psicologo);
    }
    else {
      this.errorMessage.set("Selecciona un rol válido.");
      this.isLoading.set(false);
      return;
    }

    request$.subscribe({
      next: (data: Object): void => {
        console.log('Registro exitoso:', Object);
        this.successMessage.set('¡Registro exitoso! Redirigiendo al login...');
        this.isLoading.set(false);
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 3000);
      },
      error: (error) => {
        console.error('Error en registro:', error);
        this.isLoading.set(false);

        if (error.error?.message) {
          this.errorMessage.set(error.error.message);
        } else {
          this.errorMessage.set('Error al registrar. Verifica los datos e intenta nuevamente.');
        }
      }
    })
  }

  getFieldError(fieldName: string): string | null {
    const field = this.registroForm.get(fieldName);

    if (field?.hasError('required') && field.touched) {
      return 'Este campo es requerido';
    }
    if (field?.hasError('email')) {
      return 'Email inválido';
    }
    if (field?.hasError('minlength')) {
      const minLength = field.errors?.['minlength'].requiredLength;
      return `Mínimo ${minLength} caracteres`;
    }
    if (field?.hasError('pattern')) {
      if (fieldName === 'dni') return 'DNI debe tener 8 dígitos';
      if (fieldName === 'telefono') return 'Teléfono debe tener 9 dígitos';
    }
    return null;
  }
}
