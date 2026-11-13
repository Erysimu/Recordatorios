package cl.mlopez.recordatorios.model

/** Reglas de validación del producto. Cada función devuelve null si el dato es válido
 *  o un mensaje de error comprensible si no lo es. */
object ValidadorRecordatorio {
    const val MIN_CARACTERES_TITULO = 4
    const val MAX_CARACTERES_DESCRIPCION = 200

    fun validarTitulo(titulo: String): String? =
        if (titulo.trim().length < MIN_CARACTERES_TITULO) {
            "El título debe tener al menos $MIN_CARACTERES_TITULO caracteres"
        } else null

    fun validarDescripcion(descripcion: String): String? =
        if (descripcion.length > MAX_CARACTERES_DESCRIPCION) {
            "La descripción no puede superar los $MAX_CARACTERES_DESCRIPCION caracteres"
        } else null

    fun validarCategoria(categoria: Categoria?): String? =
        if (categoria == null || categoria !in Categoria.todas) "Selecciona una categoría" else null

    fun validarPrioridad(prioridad: Prioridad?): String? =
        if (prioridad == null) "Selecciona una prioridad" else null
}
