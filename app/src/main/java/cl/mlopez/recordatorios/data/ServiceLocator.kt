package cl.mlopez.recordatorios.data

/** Punto único donde se decide qué implementación usa la app. */
object ServiceLocator {
    private val enMemoria by lazy { RecordatorioRepositoryEnMemoria() }

    val repositorio: RecordatorioRepository get() = enMemoria
    val simulacion: ControlSimulacion get() = enMemoria
}
