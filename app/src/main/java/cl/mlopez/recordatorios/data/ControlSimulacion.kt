package cl.mlopez.recordatorios.data

/** Escenarios que permiten comprobar todos los estados de la interfaz sin base de datos. */
enum class ModoSimulacion(val etiqueta: String) {
    NORMAL("Datos normales"),
    VACIO("Lista vacía"),
    ERROR_CARGA("Error al cargar"),
    ERROR_GUARDADO("Error al guardar"),
}

/** Mecanismo de simulación separado del repositorio para no mezclarlo con su contrato. */
interface ControlSimulacion {
    var modo: ModoSimulacion
}
