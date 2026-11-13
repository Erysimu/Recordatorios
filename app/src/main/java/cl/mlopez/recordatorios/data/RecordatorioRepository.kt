package cl.mlopez.recordatorios.data

import cl.mlopez.recordatorios.model.NuevoRecordatorio
import cl.mlopez.recordatorios.model.Recordatorio

/**
 * Contrato de la fuente de datos. Los ViewModel dependen de esta interfaz y no de una
 * lista concreta, por lo que más adelante se puede reemplazar por Room o un servicio
 * remoto sin modificar pantallas ni componentes.
 */
interface RecordatorioRepository {
    suspend fun obtenerRecordatorios(): List<Recordatorio>
    suspend fun obtenerPorId(id: Int): Recordatorio?
    suspend fun guardar(nuevo: NuevoRecordatorio): Recordatorio
}
