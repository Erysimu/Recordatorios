package cl.mlopez.recordatorios.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

/**
 * Campo de texto controlado: no guarda estado propio, recibe el valor y avisa cada
 * cambio mediante una lambda. El error se comunica con isError, texto y semántica.
 */
@Composable
fun CampoTexto(
    valor: String,
    onValorCambiado: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    ayuda: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    habilitado: Boolean = true,
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorCambiado,
        label = { Text(etiqueta) },
        isError = error != null,
        enabled = habilitado,
        singleLine = singleLine,
        minLines = minLines,
        supportingText = {
            when {
                error != null -> Text("Error: $error")
                ayuda != null -> Text(ayuda)
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .semantics { if (error != null) this.error(error) },
    )
}

/** Selector controlado de una opción entre varias, mostrado con FilterChip. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SelectorOpciones(
    titulo: String,
    opciones: List<T>,
    seleccionada: T?,
    textoOpcion: (T) -> String,
    onSeleccion: (T) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
    habilitado: Boolean = true,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .semantics { if (error != null) this.error(error) },
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(titulo, style = MaterialTheme.typography.titleSmall)
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            opciones.forEach { opcion ->
                FilterChip(
                    selected = opcion == seleccionada,
                    onClick = { onSeleccion(opcion) },
                    label = { Text(textoOpcion(opcion)) },
                    enabled = habilitado,
                )
            }
        }
        if (error != null) {
            Text(
                text = "Error: $error",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}
