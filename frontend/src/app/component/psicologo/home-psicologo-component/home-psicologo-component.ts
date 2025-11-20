import {Component, inject} from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import {RoutingService} from '../../../services/routing-service';

@Component({
  selector: 'app-home-psicologo-component',
  imports: [RouterModule],
  templateUrl: './home-psicologo-component.html',
  styleUrl: './home-psicologo-component.css',
})
export class HomePsicologoComponent {
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
