import { HttpInterceptorFn, HttpStatusCode } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';
import { inject } from '@angular/core';
import { Router } from '@angular/router';

export const loginInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const token = localStorage.getItem('auth_token');

  console.log('🔒 Interceptor ejecutado');
  console.log('🔑 Token encontrado:', token ? 'SÍ' : 'NO');
  console.log('🌐 URL de la petición:', req.url);

  // Clonar la petición solo si hay token
  const authReq = token
    ? req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    })
    : req;

  return next(authReq).pipe(
    catchError(error => {
      console.error('❌ Error HTTP:', error.status, error.statusText);

      switch (error.status) {
        case HttpStatusCode.Unauthorized: // 401
          console.warn('Token inválido o expirado');
          localStorage.removeItem('token');
          localStorage.removeItem('user_dni');
          router.navigate(['/login']);
          break;

        case HttpStatusCode.Forbidden: // 403
          console.warn('Acceso denegado - Permisos insuficientes');
          alert('NO TIENES PERMISOS PARA ACCEDER A ESTE RECURSO');
          break;

        case HttpStatusCode.NotFound: // 404
          console.warn('Recurso no encontrado');
          break;
      }

      return throwError(() => error);
    })
  );
};
