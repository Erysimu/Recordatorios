package cl.mlopez.recordatorios.ui.formulario

import cl.mlopez.recordatorios.model.Categoria
import cl.mlopez.recordatorios.model.Prioridad

/** Estado del envío: solo puede estar en una de estas situaciones a la vez. */
sealed interface EstadoEnvio {
    data object Inactivo : EstadoEnvio
    data object Enviando : EstadoEnvio
    data class Exito(val idCreado: Int) : EstadoEnvio
    data class Error(val mensaje: String) : EstadoEnvio
}

/** Estado inmutable de la pantalla de creación. */
data class FormularioUiState(
    val titulo: String = "",
    val descripcion: String = "",
    val categoria: Categoria? = null,
    val prioridad: Prioridad? = null,
    val errorTitulo: String? = null,
    val errorDescripcion: String? = null,
    val errorCategoria: String? = null,
    val errorPrioridad: String? = null,
    val formularioTocado: Boolean = false,
    val envio: EstadoEnvio = EstadoEnvio.Inactivo,
) {
    val esValido: Boolean
        get() = errorTitulo == null && errorDescripcion == null &&
            errorCategoria == null && errorPrioridad == null

    val enviando: Boolean get() = envio is EstadoEnvio.Enviando

    /** El botón Guardar se deshabilita si el formulario es inválido o hay un envío en curso. */
    val puedeGuardar: Boolean get() = esValido && !enviando
}

/** Eventos que la interfaz envía al ViewModel. */
sealed interface FormularioEvento {
    data class TituloCambiado(val valor: String) : FormularioEvento
    data class DescripcionCambiada(val valor: String) : FormularioEvento
    data class CategoriaSeleccionada(val categoria: Categoria) : FormularioEvento
    data class PrioridadSeleccionada(val prioridad: Prioridad) : FormularioEvento
    data object Guardar : FormularioEvento
}
