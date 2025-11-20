import {Component, inject, signal} from '@angular/core';
import {Router, RouterModule} from '@angular/router';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login-component',
  standalone: true,
  imports: [RouterModule, ReactiveFormsModule, CommonModule],
  templateUrl: './login-component.html',
  styleUrl: './login-component.css',
})
export class LoginComponent {
  router: Router = inject(Router);
  loginForm: FormGroup;
  fb = inject(FormBuilder);
  authService = inject(AuthService);

  errorMessage = signal<string>('');
  isLoading = signal<boolean>(false);

  constructor() {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    })
  }
  ngOnInit() {
    if(localStorage.getItem('token')!=null){
      localStorage.clear();//borra todos los items
      console.log("Token y items eliminados");
    }
    this.loadForm()
  }

  loadForm(): void {
    console.log("Form");
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.errorMessage.set('Por favor, completa todos los campos correctamente');
      return;
    }

    this.isLoading.set(true);
    this.errorMessage.set('');

    const credentials = {
      email: this.loginForm.value.email!,
      password: this.loginForm.value.password!
    };

    this.authService.login(credentials).subscribe({
      next: (response) => {
        console.log('Login exitoso:', response);

        // Guardar email para obtener datos después
        localStorage.setItem('user_email', response.usuario.email);
        localStorage.setItem('user_role', response.usuario.roles.toString());

        // Redirigir según el rol
        console.log("¿Es paciente?:" + this.authService.isPaciente());
        console.log("¿Es psicologo?:" + this.authService.isPsicologo());
        if (this.authService.isPaciente()) {
          this.router.navigate(['/home-paciente']);
        } else if (this.authService.isPsicologo()) {
          this.router.navigate(['/home-psicologo']);
        } else {
          this.router.navigate(['/']);
        }

        this.isLoading.set(false);
      },
      error: (error) => {
        console.error('Error en login:', error);
        this.isLoading.set(false);

        if (error.error?.message) {
          this.errorMessage.set(error.error.message);
        } else {
          this.errorMessage.set('Credenciales incorrectas. Intenta nuevamente.');
        }
      }
    });
  }
}
