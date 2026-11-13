package cl.mlopez.recordatorios.ui.listado

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.mlopez.recordatorios.data.ControlSimulacion
import cl.mlopez.recordatorios.data.ModoSimulacion
import cl.mlopez.recordatorios.data.RecordatorioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class ListadoViewModel(
    private val repositorio: RecordatorioRepository,
    private val simulacion: ControlSimulacion,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ListadoUiState>(ListadoUiState.Cargando)
    val uiState: StateFlow<ListadoUiState> = _uiState.asStateFlow()

    private val _modoSimulacion = MutableStateFlow(simulacion.modo)
    val modoSimulacion: StateFlow<ModoSimulacion> = _modoSimulacion.asStateFlow()

    init {
        cargarRecordatorios()
    }

    fun cargarRecordatorios() {
        viewModelScope.launch {
            _uiState.update { ListadoUiState.Cargando }
            val nuevoEstado = try {
                val lista = repositorio.obtenerRecordatorios()
                if (lista.isEmpty()) ListadoUiState.Vacio else ListadoUiState.ConDatos(lista)
            } catch (e: IOException) {
                ListadoUiState.Error("No pudimos cargar los recordatorios. ${e.message}")
            }
            _uiState.update { nuevoEstado }
        }
    }

    fun cambiarModoSimulacion(modo: ModoSimulacion) {
        simulacion.modo = modo
        _modoSimulacion.update { modo }
        cargarRecordatorios()
    }
}
