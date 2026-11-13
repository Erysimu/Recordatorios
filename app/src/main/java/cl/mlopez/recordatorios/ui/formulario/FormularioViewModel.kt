package cl.mlopez.recordatorios.ui.formulario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.mlopez.recordatorios.data.RecordatorioRepository
import cl.mlopez.recordatorios.model.NuevoRecordatorio
import cl.mlopez.recordatorios.model.ValidadorRecordatorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class FormularioViewModel(
    private val repositorio: RecordatorioRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(validar(FormularioUiState()))
    val uiState: StateFlow<FormularioUiState> = _uiState.asStateFlow()

    fun onEvento(evento: FormularioEvento) {
        when (evento) {
            is FormularioEvento.TituloCambiado ->
                _uiState.update { validar(it.copy(titulo = evento.valor, formularioTocado = true)) }
            is FormularioEvento.DescripcionCambiada ->
                _uiState.update { validar(it.copy(descripcion = evento.valor, formularioTocado = true)) }
            is FormularioEvento.CategoriaSeleccionada ->
                _uiState.update { validar(it.copy(categoria = evento.categoria, formularioTocado = true)) }
            is FormularioEvento.PrioridadSeleccionada ->
                _uiState.update { validar(it.copy(prioridad = evento.prioridad, formularioTocado = true)) }
            FormularioEvento.Guardar -> guardar()
        }
    }

    /** Aplica las reglas y devuelve una nueva instancia con los mensajes de error. */
    private fun validar(estado: FormularioUiState): FormularioUiState = estado.copy(
        errorTitulo = ValidadorRecordatorio.validarTitulo(estado.titulo),
        errorDescripcion = ValidadorRecordatorio.validarDescripcion(estado.descripcion),
        errorCategoria = ValidadorRecordatorio.validarCategoria(estado.categoria),
        errorPrioridad = ValidadorRecordatorio.validarPrioridad(estado.prioridad),
    )

    private fun guardar() {
        val actual = _uiState.value
        if (!actual.puedeGuardar) return
        val categoria = actual.categoria ?: return
        val prioridad = actual.prioridad ?: return

        _uiState.update { it.copy(envio = EstadoEnvio.Enviando) }
        viewModelScope.launch {
            val resultado = try {
                val creado = repositorio.guardar(
                    NuevoRecordatorio(
                        titulo = actual.titulo.trim(),
                        descripcion = actual.descripcion.trim().ifBlank { null },
                        categoria = categoria,
                        prioridad = prioridad,
                    ),
                )
                EstadoEnvio.Exito(creado.id)
            } catch (e: IOException) {
                EstadoEnvio.Error("No pudimos guardar el recordatorio. ${e.message}")
            }
            _uiState.update { it.copy(envio = resultado) }
        }
    }
}
