package cl.mlopez.recordatorios.ui.detalle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.mlopez.recordatorios.data.RecordatorioRepository
import cl.mlopez.recordatorios.ui.navigation.Rutas
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

/** Recibe solo el identificador desde la navegación y busca el registro en el repositorio. */
class DetalleViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositorio: RecordatorioRepository,
) : ViewModel() {

    private val recordatorioId: Int = checkNotNull(savedStateHandle[Rutas.ARG_ID]) {
        "Falta el argumento ${Rutas.ARG_ID}"
    }

    private val _uiState = MutableStateFlow<DetalleUiState>(DetalleUiState.Cargando)
    val uiState: StateFlow<DetalleUiState> = _uiState.asStateFlow()

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _uiState.update { DetalleUiState.Cargando }
            val nuevoEstado = try {
                repositorio.obtenerPorId(recordatorioId)
                    ?.let { DetalleUiState.Encontrado(it) }
                    ?: DetalleUiState.NoEncontrado
            } catch (e: IOException) {
                DetalleUiState.Error("No pudimos cargar el recordatorio. ${e.message}")
            }
            _uiState.update { nuevoEstado }
        }
    }
}
