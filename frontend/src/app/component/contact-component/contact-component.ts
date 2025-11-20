import { Component, inject } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router, RouterModule } from '@angular/router';
import {RoutingService} from '../../services/routing-service';

@Component({
  selector: 'app-contact-component',
  imports: [RouterModule],
  templateUrl: './contact-component.html',
  styleUrl: './contact-component.css',
})
export class ContactComponent {
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
