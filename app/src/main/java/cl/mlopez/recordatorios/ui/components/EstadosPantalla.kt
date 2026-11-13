package cl.mlopez.recordatorios.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/** Tipos de mensaje que puede mostrar una pantalla cuando no hay contenido normal. */
enum class TipoEstado { CARGA, VACIO, ERROR }

/**
 * Componente reutilizable para comunicar carga, vacío o error.
 * La acción (crear o reintentar) se informa mediante una lambda.
 */
@Composable
fun EstadoPantalla(
    tipo: TipoEstado,
    mensaje: String,
    modifier: Modifier = Modifier,
    textoAccion: String? = null,
    onAccion: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp)
            .semantics { liveRegion = LiveRegionMode.Polite },
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (tipo) {
            TipoEstado.CARGA -> CircularProgressIndicator()
            TipoEstado.VACIO -> Text("📭", style = MaterialTheme.typography.displayMedium)
            TipoEstado.ERROR -> Text("⚠️", style = MaterialTheme.typography.displayMedium)
        }
        Text(
            text = mensaje,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = if (tipo == TipoEstado.ERROR) MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.onSurface,
        )
        if (textoAccion != null && onAccion != null) {
            Button(onClick = onAccion) { Text(textoAccion) }
        }
    }
}
