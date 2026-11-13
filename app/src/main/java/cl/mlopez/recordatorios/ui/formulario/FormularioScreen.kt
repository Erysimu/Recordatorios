package cl.mlopez.recordatorios.ui.formulario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.mlopez.recordatorios.model.Categoria
import cl.mlopez.recordatorios.model.Prioridad
import cl.mlopez.recordatorios.model.ValidadorRecordatorio
import cl.mlopez.recordatorios.ui.components.CampoTexto
import cl.mlopez.recordatorios.ui.components.SelectorOpciones

@Composable
fun FormularioRoute(
    viewModel: FormularioViewModel,
    onGuardado: () -> Unit,
    onCancelar: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Cuando el guardado termina con éxito se avisa una sola vez para volver al listado.
    LaunchedEffect(uiState.envio) {
        if (uiState.envio is EstadoEnvio.Exito) onGuardado()
    }

    FormularioScreen(
        uiState = uiState,
        onEvento = viewModel::onEvento,
        onGuardar = { viewModel.onEvento(FormularioEvento.Guardar) },
        onCancelar = onCancelar,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioScreen(
    uiState: FormularioUiState,
    onEvento: (FormularioEvento) -> Unit,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val mostrarErrores = uiState.formularioTocado
    val habilitado = !uiState.enviando

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Nuevo recordatorio") },
                navigationIcon = {
                    IconButton(onClick = onCancelar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Cancelar y volver")
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CampoTexto(
                valor = uiState.titulo,
                onValorCambiado = { onEvento(FormularioEvento.TituloCambiado(it)) },
                etiqueta = "Título *",
                error = if (mostrarErrores) uiState.errorTitulo else null,
                ayuda = "Mínimo ${ValidadorRecordatorio.MIN_CARACTERES_TITULO} caracteres",
                habilitado = habilitado,
            )
            CampoTexto(
                valor = uiState.descripcion,
                onValorCambiado = { onEvento(FormularioEvento.DescripcionCambiada(it)) },
                etiqueta = "Descripción (opcional)",
                error = if (mostrarErrores) uiState.errorDescripcion else null,
                ayuda = "${uiState.descripcion.length}/${ValidadorRecordatorio.MAX_CARACTERES_DESCRIPCION}",
                singleLine = false,
                minLines = 3,
                habilitado = habilitado,
            )
            SelectorOpciones(
                titulo = "Categoría *",
                opciones = Categoria.todas.toList(),
                seleccionada = uiState.categoria,
                textoOpcion = { it.nombre },
                onSeleccion = { onEvento(FormularioEvento.CategoriaSeleccionada(it)) },
                error = if (mostrarErrores) uiState.errorCategoria else null,
                habilitado = habilitado,
            )
            SelectorOpciones(
                titulo = "Prioridad *",
                opciones = Prioridad.entries.toList(),
                seleccionada = uiState.prioridad,
                textoOpcion = { it.etiqueta },
                onSeleccion = { onEvento(FormularioEvento.PrioridadSeleccionada(it)) },
                error = if (mostrarErrores) uiState.errorPrioridad else null,
                habilitado = habilitado,
            )

            val envio = uiState.envio
            if (envio is EstadoEnvio.Error) {
                ErrorGuardado(mensaje = envio.mensaje, onReintentar = onGuardar)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedButton(onClick = onCancelar, enabled = habilitado) { Text("Cancelar") }
                Button(onClick = onGuardar, enabled = uiState.puedeGuardar) {
                    if (uiState.enviando) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                        Text("  Guardando…")
                    } else {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}

/** Error general de guardado, con texto y acción de reintento (no depende solo del color). */
@Composable
private fun ErrorGuardado(
    mensaje: String,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "⚠️ $mensaje",
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.weight(1f),
            )
            TextButton(onClick = onReintentar) { Text("Reintentar") }
        }
    }
}
