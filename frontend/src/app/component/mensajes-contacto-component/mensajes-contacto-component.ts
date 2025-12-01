import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { RoutingService } from '../../services/routing-service';
import { ContactoService } from '../../services/contacto-service';
import { ContactoMensaje } from '../../model/interfaces';

@Component({
  selector: 'app-mensajes-contacto',
  imports: [CommonModule, RouterModule],
  templateUrl: './mensajes-contacto-component.html',
  styleUrl: './mensajes-contacto-component.css',
})
export class MensajesContactoComponent implements OnInit {
  private authService = inject(AuthService);
  private roleRouting = inject(RoutingService);
  private contactoService = inject(ContactoService);

  mensajes = signal<ContactoMensaje[]>([]);
  mensajesFiltrados = signal<ContactoMensaje[]>([]);
  isLoadingData = signal<boolean>(true);
  errorMessage = signal<string>('');
  successMessage = signal<string>('');
  filtroAsunto = signal<string>('todos');
  mensajeSeleccionado = signal<ContactoMensaje | null>(null);
  showModal = signal<boolean>(false);

  asuntos = [
    'Soporte y Cuenta',
    'Consultas sobre un Servicio',
    'Problema Técnico',
    'Sugerencia o Idea',
    'General'
  ];

  ngOnInit(): void {
    this.cargarMensajes();
  }

  cargarMensajes(): void {
    this.isLoadingData.set(true);
    this.contactoService.listarTodo().subscribe({
      next: (data) => {
        // Ordenar por fecha descendente (más reciente primero)
        const mensajesOrdenados = data.sort((a: ContactoMensaje, b: ContactoMensaje) => {
          return new Date(b.fecha!).getTime() - new Date(a.fecha!).getTime();
        });
        this.mensajes.set(mensajesOrdenados);
        this.aplicarFiltro();
        this.isLoadingData.set(false);
      },
      error: (error) => {
        console.error('Error al cargar mensajes:', error);
        this.errorMessage.set('Error al cargar los mensajes de contacto');
        this.isLoadingData.set(false);
      },
    });
  }

  filtrarPorAsunto(asunto: string): void {
    this.filtroAsunto.set(asunto);
    this.aplicarFiltro();
  }

  aplicarFiltro(): void {
    const filtro = this.filtroAsunto();
    if (filtro === 'todos') {
      this.mensajesFiltrados.set(this.mensajes());
    } else {
      this.mensajesFiltrados.set(
        this.mensajes().filter((m) => m.asunto === filtro)
      );
    }
  }

  verDetalle(mensaje: ContactoMensaje): void {
    this.mensajeSeleccionado.set(mensaje);
    this.showModal.set(true);
  }

  cerrarModal(): void {
    this.showModal.set(false);
    this.mensajeSeleccionado.set(null);
  }

  formatearFecha(fecha: string | undefined): string {
    if (!fecha) return 'Sin fecha';
    const date = new Date(fecha);
    return date.toLocaleDateString('es-PE', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
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
}
