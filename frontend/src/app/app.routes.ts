import { Routes } from '@angular/router';
import { InicioComponent } from './component/inicio-component/inicio-component';
import { RegistroComponent } from './component/registro-component/registro-component';
import { LoginComponent } from './component/login-component/login-component';
import { HomePacienteComponent } from './component/paciente/home-paciente-component/home-paciente-component';
import { HomePsicologoComponent } from './component/psicologo/home-psicologo-component/home-psicologo-component';
import { AboutComponent } from './component/about-component/about-component';
import { PerfilPacienteComponent } from './component/paciente/perfil-paciente-component/perfil-paciente-component';
import { PerfilPsicologoComponent } from './component/psicologo/perfil-psicologo-component/perfil-psicologo-component';
import { ContactComponent } from './component/contact-component/contact-component';
import { SoporteComponent } from './component/soporte-component/soporte-component';
import { FaqComponent } from './component/faq-component/faq-component';
import { EspecialidadesComponent } from './component/especialidades-component/especialidades-component';
import { MensajesContactoComponent } from './component/mensajes-contacto-component/mensajes-contacto-component';
import { CitasPacienteComponent } from './component/paciente/citas-paciente-component/citas-paciente-component';
import { CitasPsicologoComponent } from './component/psicologo/citas-psicologo-component/citas-psicologo-component';

export const routes: Routes = [
  // Rutas Públicas
  { path: '', component: InicioComponent, pathMatch: 'full' },
  { path: 'registro', component: RegistroComponent },
  { path: 'login', component: LoginComponent },

  // Rutas Compartidas (requieren autenticación)
  { path: 'about', component: AboutComponent },
  { path: 'contact', component: ContactComponent },
  { path: 'soporte', component: SoporteComponent },
  { path: 'faq', component: FaqComponent },

  // Rutas Paciente
  { path: 'home-paciente', component: HomePacienteComponent },
  { path: 'perfil-paciente', component: PerfilPacienteComponent },
  { path: 'citas-paciente', component: CitasPacienteComponent },

  // Rutas Psicólogo
  { path: 'home-psicologo', component: HomePsicologoComponent },
  { path: 'perfil-psicologo', component: PerfilPsicologoComponent },
  { path: 'citas-psicologo', component: CitasPsicologoComponent },
  { path: 'especialidades', component: EspecialidadesComponent },

  // Rutas Admin
  { path: 'mensajes-contacto', component: MensajesContactoComponent },

  // Redirección por defecto
  { path: '**', redirectTo: '' }
];
