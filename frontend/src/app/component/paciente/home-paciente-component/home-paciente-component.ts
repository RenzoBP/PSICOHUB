import {Component, inject} from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import {RoutingService} from '../../../services/routing-service';

@Component({
  selector: 'app-home-paciente-component',
  imports: [RouterModule],
  templateUrl: './home-paciente-component.html',
  styleUrl: './home-paciente-component.css',
})
export class HomePacienteComponent {
  private authService = inject(AuthService);
  private roleRouting = inject(RoutingService);

  logout(): void {
    this.authService.logout();
  }

  goHome() {
    this.roleRouting.goHome();
  }

  goPerfil() {
    this.roleRouting.goPerfil();
  }
}
