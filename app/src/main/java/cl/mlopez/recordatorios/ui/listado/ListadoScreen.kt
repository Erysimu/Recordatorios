package cl.mlopez.recordatorios.ui.listado

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.mlopez.recordatorios.data.ModoSimulacion
import cl.mlopez.recordatorios.model.EstadoRecordatorio
import cl.mlopez.recordatorios.model.Prioridad
import cl.mlopez.recordatorios.model.Recordatorio
import cl.mlopez.recordatorios.model.filtrarPorEstado
import cl.mlopez.recordatorios.model.filtrarPorPrioridad
import cl.mlopez.recordatorios.ui.components.EstadoPantalla
import cl.mlopez.recordatorios.ui.components.RecordatorioCard
import cl.mlopez.recordatorios.ui.components.TipoEstado

/** Ruta: conecta el ViewModel con la pantalla. La pantalla no conoce al ViewModel. */
@Composable
fun ListadoRoute(
    viewModel: ListadoViewModel,
    onCrearRecordatorio: () -> Unit,
    onRecordatorioClick: (Int) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val modo by viewModel.modoSimulacion.collectAsStateWithLifecycle()
    ListadoScreen(
        uiState = uiState,
        modoSimulacion = modo,
        onCrearRecordatorio = onCrearRecordatorio,
        onRecordatorioClick = onRecordatorioClick,
        onReintentar = viewModel::cargarRecordatorios,
        onModoSimulacion = viewModel::cambiarModoSimulacion,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListadoScreen(
    uiState: ListadoUiState,
    modoSimulacion: ModoSimulacion,
    onCrearRecordatorio: () -> Unit,
    onRecordatorioClick: (Int) -> Unit,
    onReintentar: () -> Unit,
    onModoSimulacion: (ModoSimulacion) -> Unit,
    modifier: Modifier = Modifier,
) {
    var menuAbierto by remember { mutableStateOf(false) }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Mis recordatorios") },
                actions = {
                    IconButton(onClick = { menuAbierto = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Simular estados")
                    }
                    DropdownMenu(expanded = menuAbierto, onDismissRequest = { menuAbierto = false }) {
                        ModoSimulacion.entries.forEach { modo ->
                            DropdownMenuItem(
                                text = { Text(modo.etiqueta) },
                                leadingIcon = {
                                    if (modo == modoSimulacion) Icon(Icons.Default.Check, contentDescription = "Seleccionado")
                                },
                                onClick = {
                                    menuAbierto = false
                                    onModoSimulacion(modo)
                                },
                            )
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCrearRecordatorio) {
                Icon(Icons.Default.Add, contentDescription = "Crear recordatorio")
            }
        },
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            if (modoSimulacion != ModoSimulacion.NORMAL) {
                Surface(color = MaterialTheme.colorScheme.tertiaryContainer, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Simulación: ${modoSimulacion.etiqueta}",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    )
                }
            }
            // when exhaustivo: solo se muestra un estado a la vez.
            when (uiState) {
                ListadoUiState.Cargando -> EstadoPantalla(
                    tipo = TipoEstado.CARGA,
                    mensaje = "Cargando recordatorios…",
                )
                ListadoUiState.Vacio -> EstadoPantalla(
                    tipo = TipoEstado.VACIO,
                    mensaje = "Todavía no tienes recordatorios.",
                    textoAccion = "Crear recordatorio",
                    onAccion = onCrearRecordatorio,
                )
                is ListadoUiState.Error -> EstadoPantalla(
                    tipo = TipoEstado.ERROR,
                    mensaje = uiState.mensaje,
                    textoAccion = "Reintentar",
                    onAccion = onReintentar,
                )
                is ListadoUiState.ConDatos -> ListaRecordatorios(
                    recordatorios = uiState.recordatorios,
                    onRecordatorioClick = onRecordatorioClick,
                )
            }
        }
    }
}

@Composable
private fun ListaRecordatorios(
    recordatorios: List<Recordatorio>,
    onRecordatorioClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pendientes = recordatorios.filtrarPorEstado(EstadoRecordatorio.PENDIENTE).size
    val alta = recordatorios.filtrarPorPrioridad(Prioridad.ALTA).size
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 88.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text(
                text = "${recordatorios.size} recordatorios · $pendientes pendientes · $alta de prioridad alta",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        items(recordatorios, key = { it.id }) { recordatorio ->
            RecordatorioCard(recordatorio = recordatorio, onClick = onRecordatorioClick)
        }
    }
}
