package cl.mlopez.recordatorios.ui.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.mlopez.recordatorios.model.Recordatorio
import cl.mlopez.recordatorios.ui.components.Etiqueta
import cl.mlopez.recordatorios.ui.components.EstadoPantalla
import cl.mlopez.recordatorios.ui.components.TipoEstado
import cl.mlopez.recordatorios.ui.theme.coloresCategoria
import cl.mlopez.recordatorios.ui.theme.coloresPrioridad
import java.time.format.DateTimeFormatter

@Composable
fun DetalleRoute(
    viewModel: DetalleViewModel,
    onVolver: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DetalleScreen(uiState = uiState, onVolver = onVolver, onReintentar = viewModel::cargar)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    uiState: DetalleUiState,
    onVolver: () -> Unit,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
            )
        },
    ) { innerPadding ->
        val contenidoModifier = Modifier.fillMaxSize().padding(innerPadding)
        when (uiState) {
            DetalleUiState.Cargando -> EstadoPantalla(
                tipo = TipoEstado.CARGA,
                mensaje = "Cargando recordatorio…",
                modifier = contenidoModifier,
            )
            DetalleUiState.NoEncontrado -> EstadoPantalla(
                tipo = TipoEstado.VACIO,
                mensaje = "El recordatorio no existe.",
                textoAccion = "Volver",
                onAccion = onVolver,
                modifier = contenidoModifier,
            )
            is DetalleUiState.Error -> EstadoPantalla(
                tipo = TipoEstado.ERROR,
                mensaje = uiState.mensaje,
                textoAccion = "Reintentar",
                onAccion = onReintentar,
                modifier = contenidoModifier,
            )
            is DetalleUiState.Encontrado -> DetalleContenido(
                recordatorio = uiState.recordatorio,
                modifier = contenidoModifier,
            )
        }
    }
}

@Composable
private fun DetalleContenido(
    recordatorio: Recordatorio,
    modifier: Modifier = Modifier,
) {
    val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(recordatorio.titulo, style = MaterialTheme.typography.headlineSmall)
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
                HorizontalDivider()
                FilaDato("Descripción", recordatorio.descripcion ?: "Sin descripción")
                FilaDato("Estado", recordatorio.estado.etiqueta)
                FilaDato("Fecha límite", recordatorio.fechaLimite?.format(formato) ?: "Sin fecha")
                FilaDato("Identificador", "#${recordatorio.id}")
            }
        }
    }
}

@Composable
private fun FilaDato(etiqueta: String, valor: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(etiqueta, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(valor, style = MaterialTheme.typography.bodyLarge)
    }
}
