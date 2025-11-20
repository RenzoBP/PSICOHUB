import {Component, inject} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {RouterModule} from '@angular/router';
import {RoutingService} from '../../services/routing-service';

@Component({
  selector: 'app-faq-component',
  imports: [RouterModule],
  templateUrl: './faq-component.html',
  styleUrl: './faq-component.css',
})
export class FaqComponent {
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
