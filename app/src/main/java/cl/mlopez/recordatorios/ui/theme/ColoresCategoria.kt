package cl.mlopez.recordatorios.ui.theme

import androidx.compose.ui.graphics.Color
import cl.mlopez.recordatorios.model.Categoria
import cl.mlopez.recordatorios.model.Prioridad

/** Map que asocia cada categoría con un color. */
val coloresCategoria: Map<Categoria, Color> = mapOf(
    Categoria.Trabajo to Color(0xFF1E88E5),
    Categoria.Personal to Color(0xFF8E24AA),
    Categoria.Estudio to Color(0xFF43A047),
    Categoria.Salud to Color(0xFFE53935),
)

val coloresPrioridad: Map<Prioridad, Color> = mapOf(
    Prioridad.BAJA to Color(0xFF757575),
    Prioridad.MEDIA to Color(0xFFF9A825),
    Prioridad.ALTA to Color(0xFFD32F2F),
)
