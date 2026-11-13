package cl.mlopez.recordatorios.model

/** Operaciones sobre colecciones (List, Set y Map) usadas por la aplicación. */

fun List<Recordatorio>.filtrarPorEstado(estado: EstadoRecordatorio): List<Recordatorio> =
    filter { it.estado == estado }

fun List<Recordatorio>.filtrarPorPrioridad(prioridad: Prioridad): List<Recordatorio> =
    filter { it.prioridad == prioridad }

/** Set con las categorías que efectivamente se usan, sin repetidos. */
fun List<Recordatorio>.categoriasEnUso(): Set<Categoria> = map { it.categoria }.toSet()

/** Map que agrupa los recordatorios por categoría. */
fun List<Recordatorio>.agruparPorCategoria(): Map<Categoria, List<Recordatorio>> = groupBy { it.categoria }
