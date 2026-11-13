package cl.mlopez.recordatorios.ui.listado

import cl.mlopez.recordatorios.model.Recordatorio

/** Estados mutuamente excluyentes de la pantalla de listado. */
sealed interface ListadoUiState {
    data object Cargando : ListadoUiState
    data object Vacio : ListadoUiState
    data class ConDatos(val recordatorios: List<Recordatorio>) : ListadoUiState
    data class Error(val mensaje: String) : ListadoUiState
}
