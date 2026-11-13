package cl.mlopez.recordatorios.data

import cl.mlopez.recordatorios.model.NuevoRecordatorio
import cl.mlopez.recordatorios.model.Recordatorio
import kotlinx.coroutines.delay
import java.io.IOException

/** Implementación en memoria. Los delay simulan la latencia de una fuente real. */
class RecordatorioRepositoryEnMemoria(
    iniciales: List<Recordatorio> = DatosSimulados.recordatorios,
) : RecordatorioRepository, ControlSimulacion {

    override var modo: ModoSimulacion = ModoSimulacion.NORMAL

    private val recordatorios = iniciales.toMutableList()
    private var siguienteId = (iniciales.maxOfOrNull { it.id } ?: 0) + 1

    override suspend fun obtenerRecordatorios(): List<Recordatorio> {
        delay(1_200)
        return when (modo) {
            ModoSimulacion.ERROR_CARGA -> throw IOException("La fuente de datos no respondió.")
            ModoSimulacion.VACIO -> emptyList()
            ModoSimulacion.NORMAL, ModoSimulacion.ERROR_GUARDADO -> recordatorios.toList()
        }
    }

    override suspend fun obtenerPorId(id: Int): Recordatorio? {
        delay(600)
        if (modo == ModoSimulacion.ERROR_CARGA) throw IOException("La fuente de datos no respondió.")
        return recordatorios.firstOrNull { it.id == id }
    }

    override suspend fun guardar(nuevo: NuevoRecordatorio): Recordatorio {
        delay(1_500)
        if (modo == ModoSimulacion.ERROR_GUARDADO) throw IOException("No se pudo guardar el recordatorio.")
        val creado = Recordatorio(
            id = siguienteId++,
            titulo = nuevo.titulo,
            descripcion = nuevo.descripcion,
            categoria = nuevo.categoria,
            prioridad = nuevo.prioridad,
        )
        recordatorios.add(0, creado)
        return creado
    }
}
