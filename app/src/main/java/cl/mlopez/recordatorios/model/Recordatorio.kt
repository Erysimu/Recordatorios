package cl.mlopez.recordatorios.model

import java.time.LocalDate

/**
 * Categorías permitidas. Se modelan con una sealed class para que el compilador
 * conozca todas las opciones posibles y los `when` sean exhaustivos.
 */
sealed class Categoria(val nombre: String) {
    data object Trabajo : Categoria("Trabajo")
    data object Personal : Categoria("Personal")
    data object Estudio : Categoria("Estudio")
    data object Salud : Categoria("Salud")

    companion object {
        /** Set: garantiza que no existan categorías repetidas. Se usa lazy para evitar
         *  problemas de orden de inicialización entre la sealed class y sus objetos. */
        val todas: Set<Categoria> by lazy { linkedSetOf(Trabajo, Personal, Estudio, Salud) }

        fun desdeNombre(nombre: String): Categoria? = todas.firstOrNull { it.nombre == nombre }
    }
}

enum class Prioridad(val etiqueta: String) {
    BAJA("Baja"),
    MEDIA("Media"),
    ALTA("Alta"),
}

enum class EstadoRecordatorio(val etiqueta: String) {
    PENDIENTE("Pendiente"),
    EN_PROGRESO("En progreso"),
    COMPLETADO("Completado"),
}

/** Recordatorio inmutable: cualquier cambio genera una nueva instancia con copy(). */
data class Recordatorio(
    val id: Int,
    val titulo: String,
    val descripcion: String? = null,
    val categoria: Categoria,
    val prioridad: Prioridad,
    val estado: EstadoRecordatorio = EstadoRecordatorio.PENDIENTE,
    val fechaLimite: LocalDate? = null,
)

/** Datos que ingresa la persona usuaria antes de que el repositorio asigne un id. */
data class NuevoRecordatorio(
    val titulo: String,
    val descripcion: String?,
    val categoria: Categoria,
    val prioridad: Prioridad,
)
