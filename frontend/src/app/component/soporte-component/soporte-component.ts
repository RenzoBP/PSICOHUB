import {Component, inject} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {RouterModule} from '@angular/router';
import {RoutingService} from '../../services/routing-service';

@Component({
  selector: 'app-soporte-component',
  imports: [RouterModule],
  templateUrl: './soporte-component.html',
  styleUrl: './soporte-component.css',
})
export class SoporteComponent {
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
