import { Routes } from '@angular/router';
import { InicioComponent } from './component/inicio-component/inicio-component';
import { RegistroComponent } from './component/registro-component/registro-component';
import { LoginComponent } from './component/login-component/login-component';
import { HomePacienteComponent } from './component/paciente/home-paciente-component/home-paciente-component';
import { HomePsicologoComponent } from './component/psicologo/home-psicologo-component/home-psicologo-component';
import { AboutComponent } from './component/about-component/about-component';
import { PerfilPacienteComponent } from './component/paciente/perfil-paciente-component/perfil-paciente-component';
import { AgendarCitasPacienteComponent } from './component/paciente/agendar-citas-paciente-component/agendar-citas-paciente-component';
import {ContactComponent} from './component/contact-component/contact-component';
import {SoporteComponent} from './component/soporte-component/soporte-component';
import {FaqComponent} from './component/faq-component/faq-component';
import {PerfilPsicologoComponent} from './component/psicologo/perfil-psicologo-component/perfil-psicologo-component';

export const routes: Routes = [
  { path: '', component: InicioComponent, pathMatch: 'full' },
  { path: 'registro', component: RegistroComponent },
  { path: 'login', component: LoginComponent },
  { path: 'about', component: AboutComponent },
  { path: 'contact', component: ContactComponent },
  { path: 'soporte', component: SoporteComponent },
  { path: 'faq', component: FaqComponent },
  { path: 'home-paciente', component: HomePacienteComponent },
  { path: 'home-psicologo', component: HomePsicologoComponent },
  { path: 'perfil-paciente', component: PerfilPacienteComponent },
  { path: 'perfil-psicologo', component: PerfilPsicologoComponent },
  { path: 'agendar-citas-paciente', component: AgendarCitasPacienteComponent },

];
