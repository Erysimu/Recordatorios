package cl.mlopez.recordatorios.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cl.mlopez.recordatorios.model.EstadoRecordatorio
import cl.mlopez.recordatorios.model.Recordatorio
import cl.mlopez.recordatorios.ui.theme.coloresCategoria
import cl.mlopez.recordatorios.ui.theme.coloresPrioridad

/** Componente reutilizable que representa un recordatorio en el listado. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordatorioCard(
    recordatorio: Recordatorio,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val completado = recordatorio.estado == EstadoRecordatorio.COMPLETADO
    Card(
        onClick = { onClick(recordatorio.id) },
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = "${recordatorio.titulo}. Categoría ${recordatorio.categoria.nombre}, " +
                    "prioridad ${recordatorio.prioridad.etiqueta}, estado ${recordatorio.estado.etiqueta}"
            },
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = recordatorio.titulo,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textDecoration = if (completado) TextDecoration.LineThrough else null,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Etiqueta(
                    texto = recordatorio.categoria.nombre,
                    color = coloresCategoria[recordatorio.categoria] ?: Color.Gray,
                )
                Etiqueta(
                    texto = "Prioridad ${recordatorio.prioridad.etiqueta}",
                    color = coloresPrioridad[recordatorio.prioridad] ?: Color.Gray,
                )
            }
            Text(
                text = "Estado: ${recordatorio.estado.etiqueta}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
