package cl.mlopez.recordatorios.ui.navigation

/** Rutas del grafo de navegación. Al detalle solo viaja el identificador. */
object Rutas {
    const val LISTADO = "listado"
    const val CREAR = "crear"
    const val ARG_ID = "recordatorioId"
    const val DETALLE = "detalle/{$ARG_ID}"

    fun detalle(id: Int): String = "detalle/$id"

    /** Clave para avisar al listado que debe recargarse después de guardar. */
    const val RESULTADO_CREADO = "recordatorio_creado"
}
