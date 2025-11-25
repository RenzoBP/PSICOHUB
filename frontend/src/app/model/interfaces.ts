export interface Usuario {
  id: number;
  email: string;
  dni: string;
  roles: string[];
}

export interface AuthResponse {
  token: string;
  usuario: Usuario;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
}

export class Paciente {
  idPaciente: number = 0;
  nombre!: string;
  apellido!: string;
  dni!: string;
  fechaNacimiento!: string;
  genero!: string;
  distrito!: string;
  direccion!: string;
  telefono!: string;
  email!: string;
  password!: string;
  activo: boolean = true;
  usuario!: Usuario;
}

export class Psicologo {
  idPsicologo: number = 0;
  nombre!: string;
  apellido!: string;
  dni!: string;
  fechaNacimiento!: string;
  genero!: string;
  distrito!: string;
  direccion!: string;
  telefono!: string;
  email!: string;
  password!: string;
  activo: boolean = true;
  usuario!: Usuario;
}

export class ContactoMensaje {
  idMensaje?: number = 0;
  nombre!: string;
  email!: string;
  asunto!: string;
  mensaje!: string;
  fecha?: string;
}

export interface ErrorResponse {
  success: boolean;
  error: string;
  message: string;
  timestamp: string;
}
