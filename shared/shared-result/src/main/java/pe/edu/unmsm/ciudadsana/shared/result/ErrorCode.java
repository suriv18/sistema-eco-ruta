package pe.edu.unmsm.ciudadsana.shared.result;

public enum ErrorCode {

    // Auth
    CREDENCIALES_INVALIDAS("Las credenciales proporcionadas son incorrectas"),
    USUARIO_NO_ENCONTRADO("Usuario no encontrado"),
    USUARIO_YA_EXISTE("Ya existe un usuario con ese email o username"),
    USUARIO_INACTIVO("El usuario está inactivo"),
    USUARIO_BLOQUEADO("El usuario está bloqueado"),
    USUARIO_SIN_PERMISO("El usuario no tiene permiso para realizar esta operación"),
    TENANT_INVALIDO("Tenant inválido o no encontrado"),
    TOKEN_INVALIDO("Token inválido o expirado"),
    REFRESH_TOKEN_INVALIDO("Refresh token inválido o expirado"),
    ROL_NO_ENCONTRADO("Rol no encontrado"),
    ROL_DUPLICADO("Ya existe un rol con ese código"),
    PERMISO_NO_ENCONTRADO("Permiso no encontrado"),
    PERMISO_DUPLICADO("Ya existe un permiso con ese código"),

    // Operacion
    ZONA_NO_ENCONTRADA("Zona no encontrada"),
    ZONA_FUERA_DE_DISTRITO("La zona no pertenece al distrito indicado"),
    DISTRITO_NO_ENCONTRADO("Distrito no encontrado"),
    DEPOSITO_NO_ENCONTRADO("Depósito no encontrado"),
    CONTENEDOR_NO_ENCONTRADO("Contenedor no encontrado"),
    UNIDAD_NO_ENCONTRADA("Unidad no encontrada"),
    UNIDAD_NO_DISPONIBLE("La unidad no está disponible"),
    UNIDAD_EN_MANTENIMIENTO("La unidad está en mantenimiento"),
    CHOFER_NO_ENCONTRADO("Chofer no encontrado"),
    CHOFER_NO_DISPONIBLE("El chofer no está disponible en ese horario"),
    TURNO_NO_ENCONTRADO("Turno no encontrado"),
    TURNO_INVALIDO("Los datos del turno son inválidos"),
    TURNO_SUPERPUESTO("El turno se superpone con otro ya registrado"),
    CAPACIDAD_EXCEDIDA("La capacidad de la unidad sería excedida"),
    VENTANA_HORARIA_INVALIDA("La ventana horaria es inválida (fin debe ser posterior a inicio)"),
    HORARIO_NO_ENCONTRADO("Horario de recolección no encontrado"),
    HORARIO_DUPLICADO("Ya existe un horario de recolección con los mismos datos"),
    HORARIO_RANGO_INVALIDO("El rango horario es inválido (fin debe ser posterior a inicio)"),

    // Ciudadano
    ALERTA_NO_ENCONTRADA("Alerta no encontrada"),
    ALERTA_DUPLICADA("Existe una alerta similar registrada recientemente en la misma zona"),
    ALERTA_FUERA_DE_GEOCERCA("La alerta está fuera del área de cobertura"),
    TRANSICION_ESTADO_INVALIDA("La transición de estado no está permitida"),

    // Rutas
    RUTA_NO_ENCONTRADA("Ruta no encontrada"),
    RUTA_NO_FACTIBLE("No se encontró solución factible para la ruta"),
    RUTA_FINALIZADA_NO_MODIFICABLE("Una ruta finalizada no puede modificarse"),
    RUTA_ESTADO_INVALIDO("El estado actual de la ruta no permite esta operación"),
    RUTA_SIN_PARADAS("La ruta debe tener al menos una parada"),
    MOTIVO_REOPTIMIZACION_REQUERIDO("Se requiere un motivo para reoptimizar la ruta"),

    // Optimizacion / Integracion
    OPTIMIZADOR_NO_DISPONIBLE("El servicio de optimización no está disponible"),
    SOLUCION_NO_FACTIBLE("El optimizador no encontró solución factible"),
    OSRM_NO_DISPONIBLE("El servicio OSRM no está disponible"),

    // General
    GEOMETRIA_INVALIDA("La geometría proporcionada es inválida"),
    ERROR_PERSISTENCIA("Error al persistir los datos"),
    VALIDACION_ERROR("Error de validación en los datos de entrada"),
    RECURSO_NO_ENCONTRADO("Recurso no encontrado"),
    OPERACION_NO_PERMITIDA("Operación no permitida en el estado actual");

    private final String mensajeDefecto;

    ErrorCode(String mensajeDefecto) {
        this.mensajeDefecto = mensajeDefecto;
    }

    public String getMensajeDefecto() {
        return mensajeDefecto;
    }
}
