package cl.mlopez.recordatorios.data

import cl.mlopez.recordatorios.model.Categoria
import cl.mlopez.recordatorios.model.EstadoRecordatorio
import cl.mlopez.recordatorios.model.Prioridad
import cl.mlopez.recordatorios.model.Recordatorio
import java.time.LocalDate

/** Datos simulados iniciales (List) mientras no existe persistencia local ni servicio remoto. */
object DatosSimulados {
    val recordatorios: List<Recordatorio> = listOf(
        Recordatorio(
            id = 1,
            titulo = "Entregar informe mensual",
            descripcion = "Enviar el informe de accesos del mes a la jefatura.",
            categoria = Categoria.Trabajo,
            prioridad = Prioridad.ALTA,
            estado = EstadoRecordatorio.EN_PROGRESO,
            fechaLimite = LocalDate.now().plusDays(2),
        ),
        Recordatorio(
            id = 2,
            titulo = "Estudiar para la prueba de redes",
            descripcion = "Repasar subnetting y VLAN.",
            categoria = Categoria.Estudio,
            prioridad = Prioridad.MEDIA,
            fechaLimite = LocalDate.now().plusDays(5),
        ),
        Recordatorio(
            id = 3,
            titulo = "Hora al dentista",
            categoria = Categoria.Salud,
            prioridad = Prioridad.ALTA,
        ),
        Recordatorio(
            id = 4,
            titulo = "Comprar regalo de cumpleaños",
            descripcion = "Buscar algo antes del fin de semana.",
            categoria = Categoria.Personal,
            prioridad = Prioridad.BAJA,
            estado = EstadoRecordatorio.COMPLETADO,
        ),
        Recordatorio(
            id = 5,
            titulo = "Actualizar CV",
            categoria = Categoria.Personal,
            prioridad = Prioridad.MEDIA,
        ),
    )
}
